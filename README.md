# JFindPwndHashes
A tool that matches hashes extracted from NTDS.dit against Troy Hunts HIBP NTLM hash collection using Java concurrency.

### Usage
Parameters:
```
 -a,--adhashes <FILE>         path to ntds.dit hash file, required
 -h,--help                    print this message
 -o,--outputfilename <FILE>   name of the output file (will be written to
                              user profile directory)
 -p,--pwndhashes <FILE>       path to hibp ntlm hash file, required
```

### Get the list with pwnd NTLM hashes 

The list can be downloaded here: https://haveibeenpwned.com/Passwords

### Extract and prepare NTLM hashes from ntds.dit

1. Dump NTDS.dit on a DC:
```
C:\>mdkir c:\windows\temp\dmp\
C:\>ntdsutil
ntdsutil: activate instance ntds
ntdsutil: ifm
ifm: create full c:\windows\temp\dmp\
ifm: quit
ntdsutil: quit
```
2. Extract hashes using `impacket-secretsdump`:
```
impacket-secretsdump -ntds Active\ Directory/ntds.dit -system registry/SYSTEM -outputfile hashes LOCAL
```
3. Separate user accounts:
```
grep -v "\\$" hashes.ntds > hashes.ntds.users
```
4. Filter disabled user accounts (requires Active Directory Powershell cmdlets):
```powershell
$accounts = Get-Content -Path .\hashes.ntds.users -Encoding "UTF8"
$results=@()
$forest=[system.directoryservices.activedirectory.forest]::GetCurrentForest().Name+':3268'

ForEach ($account in $accounts ) {
	$current = $account| Select-String -Pattern "(?<=\\)(.*?)(?=\:)" -AllMatches | Select-Object -Expand matches | Select-Object -Expand Value
	if($current -ne $null -and (Get-ADUser -filter {SamAccountName -eq $current } -Properties SamAccountName,Enabled -Server $forest).Enabled) {
		$results += $account
	} 
}
$results | Out-File "hashes.ntds.users.enabled" -Encoding "UTF8"
```