# JFindPwndHashes
A tool that matches hashes extracted from NTDS.dit against Troy Hunts HIBP NTLM hash collection using Java concurrency.

### Usage
Parameters:
```
  -p, --pwned-hashes=<pwndHashesFile>
                  path to hibp ntlm hash file, required
  -a, --ad-hashes=<adHashesFile>
                  path to prepared ntlm hash file, required
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
```

### Get the list with pwnd NTLM hashes 

The list can be downloaded here: https://haveibeenpwned.com/Passwords

### Extract and prepare NTLM hashes from ntds.dit

1. Dump NTDS.dit on a DC:
```
C:\>mkdir c:\windows\temp\dump\
C:\>ntdsutil "activate instance ntds" "ifm" "create full c:\windows\temp\dump" "quit" "quit"
```
2. Extract hashes using `impacket-secretsdump`:
```
sudo impacket-secretsdump -ntds Active\ Directory/ntds.dit -system registry/SYSTEM -outputfile hashes LOCAL
```
3. Separate user accounts:
```
grep -v "\\$" hashes.ntds > hashes.ntds.users
```
4. Filter disabled and expired user accounts (requires Active Directory Powershell cmdlets):
```powershell
$accounts = Get-Content -Path .\hashes.ntds.users -Encoding "UTF8"
$results=@()
$forest=[system.directoryservices.activedirectory.forest]::GetCurrentForest().Name+':3268'

ForEach ($account in $accounts ) {
    $current = $account| Select-String -Pattern "(?<=\\)(.*?)(?=\:)" -AllMatches | Select-Object -Expand matches | Select-Object -Expand Value
    if($null -ne $current) {

        $currentAdUser = Get-ADUser -filter {SamAccountName -eq $current } -Properties SamAccountName,Enabled,AccountExpirationDate -Server $forest

        if($currentAdUser.Enabled -and ($null -eq $currentAdUser.AccountExpirationDate -or $currentAdUser.AccountExpirationDate -gt (Get-Date))) {
            $results += $account
        }
    }
}
$results | Out-File "hashes.ntds.users.enabled" -Encoding "UTF8"
```

### Execute application
```
java -jar JFindPwndHashes.jar -a hashes.ntds.users.enabled -p pwned-passwords-ntlm.txt
```
