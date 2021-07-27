export EZYFOX_SERVER_HOME=/Users/tvd12/Documents/tvd12/java/deploy/ezyfox-server
mvn -pl . clean install
mvn -pl freetank-common -Pexport clean install
mvn -pl freetank-app-api -Pexport clean install
mvn -pl freetank-app-entry -Pexport clean install
mvn -pl freetank-plugin -Pexport clean install
cp freetank-zone-settings.xml $EZYFOX_SERVER_HOME/settings/zones/