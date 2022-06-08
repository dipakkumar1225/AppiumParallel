ECHO OFF
Set /A appPackage
FOR /F "tokens=2" %%G IN ('aapt dump badging %1 ^| FIND "package: name="') DO set extractedPkgName=%%G
FOR /F "tokens=2 delims='" %%G IN ("%extractedPkgName%") DO SET appPackage=%%G
echo %appPackage%

Set /A appLaunchActivity
FOR /F "tokens=2" %%G IN ('aapt dump badging %1 ^| FIND "launchable-activity: name="') DO set extractedLaunchActivity=%%G
FOR /F "tokens=2 delims='" %%G IN ("%extractedLaunchActivity%") DO SET appLaunchActivity=%%G
echo %appLaunchActivity%

