# freetank

# Require

1. Java 8 or higher
2. Apache Maven 3.3+

# Description
1. freetank-app-api: contains app's request controller, app's event handler and which components just related to app
2. freetank-app-entry: contains `AppEntryLoader` class, you should not add classes here
2.1 freetank-app-entry/config/config.properties: app's configuration file
3. freetank-common: contains components that use by both app and plugin
4. freetank-plugin: contains plugin's event handler, plugin's request controller and which components just related to plugin. You will need handle `USER_LOGIN` event here
4.1 freetank-plugin/config/config.properties: plugin's configuration file
5. freetank-startup: contains `ApplicationStartup` class to run on local, you should not add classes here
5.1 freetank-startup/src/main/resources/log4j.properties: log4j configuration file

# How to build?

You can build by:
1. Your IDE
2. Run `mvn clean install` on your terminal
3. Run `build.sh` file on your terminal

# How to run?


You just move to `freetank-startup` module and run `ApplicationStartup`


To run by `ezyfox-server` you need follow by steps:
1. Download [ezyfox-sever](https://resources.tvd12.com/)
2. Setup `EZYFOX_SERVER_HOME` environment variable: let's say you place `ezyfox-server` at `/Programs/ezyfox-server` so `EZYFOX_SERVER_HOME = /Programs/ezyfox-server`
3. Run `build.sh` file on your terminal
4. Copy `freetank-zone-settings.xml` to `EZYFOX_SERVER_HOME/settings/zones` folder
5. Open file `EZYFOX_SERVER_HOME/settings/ezy-settings.xml` and add to `<zones>` tag:
```xml
    <zone>
		<name>freetank</name>
		<config-file>freetank-zone-settings.xml</config-file>
		<active>true</active>
	</zone>
```
6. Run `console.sh` in `EZYFOX_SERVER_HOME` on your termial, if you want to run `ezyfox-server` in backgroud you will need run `start-server.sh` on your terminal

# Deploy mapping
Modules after will deploy to `ezyfox-server` will be mapped like this:
1. freetank-app-api => `ezyfox-server/apps/common/freetank-app-api-1.1.0.jar`
2. freetank-app-entry => `ezyfox-server/apps/entries/freetank-app`
3. freetank-common => `ezyfox-server/common/ freetank-common-1.1.0.jar`
4. freetank-plugin => `ezyfox-server/plugins/freetank-plugin`

# How to test?

On your IDE, you need:
1. Move to `freetank-startup` module 
2. Run `ApplicationStartup` in `src/main/java`
3. Run `ClientTest` in `src/test/java`