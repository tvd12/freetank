#export EZYFOX_SERVER_HOME=
mvn -pl . clean install
mvn -pl freetank-common -Pexport clean install
mvn -pl freetank-app-api -Pexport clean install
mvn -pl freetank-app-entry -Pexport clean install
mvn -pl freetank-plugin -Pexport clean install