# DB Allocator
Readme TODO

# Releasing

To release the DB Allocator jar into the
[public Maven repository](https://repository.jboss.org/nexus/content/repositories/public/org/jboss/qa/):

1. Ensure you have the permissions to push to this repository, and permissions
to release, as per the [Wildfly Extras](https://developer.jboss.org/wiki/WildflyExtrasRepository)
document.
1. Once you get the permissions to push, enter username/password credentials into your
 `~/.m2/settings.xml` file.
1. Ensure you have the latest commits
1. Issue `mvn release:prepare -Darguments="-DskipTests"` and enter the version
as well as future versions. Use [semantic versioning](https://semver.org/).
1. Issue `mvn release:perform -Darguments="-DskipTests"` to actually perform the release.
1. Continue in the [staging repository](https://repository.jboss.org/nexus/index.html#stagingRepositories) as per
the wildfly extras tutorial.
