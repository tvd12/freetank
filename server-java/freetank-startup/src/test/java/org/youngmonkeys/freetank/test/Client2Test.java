package org.youngmonkeys.freetank.test;

import com.tvd12.ezyfox.core.constant.EzyResponseCommands;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.EzyClients;
import com.tvd12.ezyfoxserver.client.EzyUTClient;
import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.entity.EzyApp;
import com.tvd12.ezyfoxserver.client.handler.EzyAppAccessHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyHandshakeHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyLoginSuccessHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyUdpHandshakeHandler;
import com.tvd12.ezyfoxserver.client.request.EzyAppAccessRequest;
import com.tvd12.ezyfoxserver.client.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.setup.EzyAppSetup;
import com.tvd12.ezyfoxserver.client.setup.EzySetup;
import com.tvd12.ezyfoxserver.client.socket.EzyMainEventsLoop;
import org.youngmonkeys.freetank.ApplicationStartup;
import org.youngmonkeys.freetank.app.constant.Commands;

public class Client2Test {

	private static String username = "Monkey2";

	public static void main(String[] args) {
		if(args.length > 0) {
			username = args[0];
		}
		EzyClientConfig config = EzyClientConfig.builder()
				.zoneName(ApplicationStartup.ZONE_APP_NAME)
				.build();
		EzyClient client = new EzyUTClient(config);
		setupClient(client);
		EzyClients.getInstance().addClient(client);
		client.connect("tvd12.com", 3005);
//		client.connect("127.0.0.1", 3005);
		EzyMainEventsLoop mainEventsLoop = new EzyMainEventsLoop();
		mainEventsLoop.start(5);
	}
	
	public static void setupClient(EzyClient client) {
		EzySetup setup = client.setup();
		setup.addDataHandler(EzyCommand.HANDSHAKE, new EzyHandshakeHandler() {
			
			@Override
			protected EzyRequest getLoginRequest() {
				return new EzyLoginRequest(
					ApplicationStartup.ZONE_APP_NAME,
					username,
					"YoungMonkey"
				);
			}
		});
		setup.addDataHandler(EzyCommand.LOGIN, new EzyLoginSuccessHandler() {
			@Override
			protected void handleLoginSuccess(EzyData responseData) {
				client.udpConnect(2611);
			}
		});
		setup.addDataHandler(EzyCommand.UDP_HANDSHAKE, new EzyUdpHandshakeHandler() {
			@Override
			protected void onAuthenticated(EzyArray data) {
				client.send(new EzyAppAccessRequest(ApplicationStartup.ZONE_APP_NAME));
			}
		});
		setup.addDataHandler(EzyCommand.APP_ACCESS, new EzyAppAccessHandler() {
			@Override
			protected void postHandle(EzyApp app, EzyArray data) {
				app.send(Commands.ACCESS_GAME, EzyEntityFactory.EMPTY_OBJECT);
			}
		});
		
		EzyAppSetup appSetup = setup.setupApp(ApplicationStartup.ZONE_APP_NAME);
		appSetup.addDataHandler(EzyResponseCommands.ERROR, (app, data) -> {
			System.out.println("error: " + data);
		});
		appSetup.addDataHandler(Commands.ACCESS_GAME, (app, data) -> {
			System.out.println("hello: " + data);
			app.udpSend(
				Commands.SYNC_POSITION,
				EzyEntityFactory.newArrayBuilder()
					.append(
						"character",
						1,
						EzyEntityFactory.newArrayBuilder().append(1, 2, 3),
						EzyEntityFactory.newArrayBuilder().append(4, 5, 6)
					)
					.build()
			);
			app.send(
				Commands.SYNC_DATA,
				EzyEntityFactory.newObjectBuilder()
					.append("command", "hello")
					.append("data", "world")
					.build()
			);
		});
		appSetup.addDataHandler(Commands.PLAYER_ACCESS_GAME, (app, data) -> {
			System.out.println("player access: " + data);
		});
		appSetup.addDataHandler(Commands.SYNC_POSITION, (app, data) -> {
			System.out.println("position: " + data);
		});
		appSetup.addDataHandler(Commands.SYNC_DATA, (app, data) -> {
			System.out.println("position: " + data);
		});
		appSetup.addDataHandler(Commands.PLAYER_EXIT_GAME, (app, data) -> {
			System.out.println("player exit game: " + data);
			app.send(Commands.PLAYER_EXIT_GAME);
		});
	}
}
