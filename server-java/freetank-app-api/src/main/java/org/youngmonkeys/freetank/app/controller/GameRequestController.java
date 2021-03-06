package org.youngmonkeys.freetank.app.controller;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.exception.BadRequestException;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.io.EzyLists;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.gamebox.constant.PlayerRole;
import com.tvd12.gamebox.entity.LocatedPlayer;
import com.tvd12.gamebox.entity.LocatedRoom;
import org.youngmonkeys.freetank.app.constant.Commands;
import org.youngmonkeys.freetank.app.constant.Errors;
import org.youngmonkeys.freetank.app.request.SyncDataRequest;
import org.youngmonkeys.freetank.app.request.SyncPositionRequest;
import org.youngmonkeys.freetank.app.service.GameService;

import java.util.Collections;
import java.util.List;

@EzyRequestController
public class GameRequestController extends EzyLoggable {

	@EzyAutoBind
	private GameService gameService;

	@EzyAutoBind
	private EzyResponseFactory responseFactory;

	@EzyDoHandle(Commands.ACCESS_GAME)
	public void userAccessGame(EzyUser user) {
		LocatedRoom room;
		LocatedPlayer player;
		synchronized (gameService) {
			room = gameService.findAvailableRoom(user);
			player = gameService.getPlayer(user.getName());
		}
		List<String> playerNames;
		synchronized (room) {
			playerNames = room.getPlayerManager().getPlayerNames();
		}
		responseFactory.newObjectResponse()
			.command(Commands.ACCESS_GAME)
			.param("roomId", room.getId())
			.param("master", player.getRole() == PlayerRole.MASTER)
			.param("playerNames", playerNames)
			.user(user)
			.execute();
		responseFactory.newObjectResponse()
			.command(Commands.PLAYER_ACCESS_GAME)
			.param("playerName", user.getName())
			.usernames(EzyLists.filter(playerNames, it -> !it.equals(user.getName())))
			.execute();
	}

	@EzyDoHandle(Commands.PLAYER_EXIT_GAME)
	public void userExitGame(EzyUser user) {
		logger.info("freetank app: user {} removed", user);
		LocatedRoom room;
		List<String> playerNames;
		String playerName = user.getName();
		synchronized (gameService) {
			room = gameService.removePlayer(playerName);
		}
		if(room == null) {
			return;
		}
		synchronized (room) {
			playerNames = room.getPlayerManager().getPlayerNames();
		}
		responseFactory.newObjectResponse()
			.command(Commands.PLAYER_EXIT_GAME)
			.param("playerName", playerName)
			.usernames(playerNames)
			.execute();
	}

	@EzyDoHandle(Commands.SYNC_POSITION)
	public void synPosition(EzyUser user, SyncPositionRequest request) {
		LocatedPlayer player;
		synchronized (gameService) {
			player = gameService.getPlayer(user.getName());
		}
		if(player == null) {
			throw new BadRequestException(Errors.UNKNOWN, "you're a hacker");
		}
		LocatedRoom room;
		synchronized (gameService) {
			room = gameService.getRoom(player.getCurrentRoomId());
		}
		List<String> playerNames;
		synchronized (room) {
			playerNames = room.getPlayerManager().getPlayerNames();
		}
		responseFactory.newArrayResponse()
			.command(Commands.SYNC_POSITION)
			.data(request)
			.usernames(EzyLists.filter(playerNames, it -> !it.equals(user.getName())))
			.execute();
	}

	@EzyDoHandle(Commands.SYNC_DATA)
	public void synData(EzyUser user, SyncDataRequest request) {
		LocatedPlayer player;
		synchronized (gameService) {
			player = gameService.getPlayer(user.getName());
		}
		if(player == null) {
			throw new BadRequestException(Errors.UNKNOWN, "you're a hacker");
		}
		List<String> playerNames;
		LocatedRoom room;
		synchronized (gameService) {
			room = gameService.getRoom(player.getCurrentRoomId());
		}
		synchronized (room) {
			playerNames = room.getPlayerManager().getPlayerNames();
		}
		responseFactory.newObjectResponse()
			.udpTransport()
			.command(Commands.SYNC_DATA)
			.data(
				EzyEntityFactory.newObjectBuilder()
					.append("command", request.getCommand())
					.append("data", request.getData())
					.build()
			)
			.usernames(EzyLists.filter(playerNames, it -> !it.equals(user.getName())))
			.execute();
	}
}
