@echo off

net stop webclient
reg add HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\WebClient\Parameters /f /v BasicAuthLevel /t reg_dword /d 2 
reg add HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\WebClient\Parameters /f /v FileSizeLimitInBytes /t reg_dword /d 4294967295
net start webclient
sc config webclient start=auto

net use w: {webDavUrl} /user:"{user}"