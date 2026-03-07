#!/bin/bash
if [ $1 = 'major' ]
then
mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.nextMajorVersion}.0.0
elif [ $1 = 'minor' ]
then
mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion}.0
elif [ $1 = 'patch' ]
then
mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion}
else
echo "unknown parameter"
fi
mvn versions:commit
VERSION=$(mvn -Dexec.executable='echo' -Dexec.args='${project.version}' --non-recursive exec:exec -q)
git commit -am "$VERSION"
git tag -a "v$VERSION" -m "$VERSION"
git push origin --tags