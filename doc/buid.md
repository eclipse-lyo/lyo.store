# Build instructions

## Useful maven commands:

mvn org.owasp:dependency-check-maven:check

mvn git-version-insert:insert-version

mvn versions:set -DnewVersion=2.3.0-SNAPSHOT

mvn -DskipTests dependency:tree -Dverbose -o

mvn clean package javadoc:aggregate

mvn verify gpg:sign

mvn dependency:analyze -Xlint:unchecked

mvn appassembler:assemble

mvn install -P platform-mars