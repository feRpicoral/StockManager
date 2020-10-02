'Start the application without showing the CMD window in the background - Windows Only'
Set WshShell = CreateObject("WScript.Shell")
WshShell.Run chr(34) & ".\bin\StockManager.bat" & Chr(34), 0
Set WshShell = Nothing