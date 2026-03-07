if %1==major (
    call mvn build-helper:parse-version versions:set -DnewVersion=${parsedVersion.nextMajorVersion}.0.0
)
if %1==minor (
    call mvn build-helper:parse-version versions:set -DnewVersion=${parsedVersion.majorVersion}.${parsedVersion.nextMinorVersion}.0
)
if %1==patch (
    call mvn build-helper:parse-version versions:set -DnewVersion=${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.nextIncrementalVersion}
)
call mvn versions:commit
for /f %%i in ('call mvn -q --non-recursive "-Dexec.executable=cmd" "-Dexec.args=/C echo ${project.version}" "exec:exec"') do set VERSION=%%i
call git commit -am "%VERSION%"
call git tag -a "v%VERSION%" -m "%VERSION%"
call git push origin --tags