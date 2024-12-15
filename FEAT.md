Here's an exhaustive list of **FTP commands** including their command structures, response structures, and relevant RFCs. This includes both standard commands and extended commands, as defined in various RFCs such as RFC 959 (the primary FTP RFC) and extended RFCs like RFC 2228, RFC 2428, RFC 3659, etc.

### Comprehensive FTP Command List with Structure and Responses

#### **Standard FTP Commands (RFC 959)**
1. **USER**
   - **Command Structure**:  
     ```
     USER <username>
     ```
   - **Response Structure**:  
     ```
     331 User name okay, need password.
     ```
     - **Explanation**: The server requests a password for the given username.

2. **PASS**
   - **Command Structure**:  
     ```
     PASS <password>
     ```
   - **Response Structure**:  
     ```
     230 User logged in, proceed.
     ```
     - **Explanation**: The server accepts the password and logs the user in.

3. **ACCT**
   - **Command Structure**:  
     ```
     ACCT <account>
     ```
   - **Response Structure**:  
     ```
     230 Account logged in, proceed.
     ```
     - **Explanation**: The server processes the account information after login (usually optional).

4. **CWD**
   - **Command Structure**:  
     ```
     CWD <directory>
     ```
   - **Response Structure**:  
     ```
     250 Requested file action okay, completed.
     ```
     - **Explanation**: The server acknowledges the change to the specified directory.

5. **CDUP**
   - **Command Structure**:  
     ```
     CDUP
     ```
   - **Response Structure**:  
     ```
     250 Requested file action okay, completed.
     ```
     - **Explanation**: The server acknowledges moving to the parent directory.

6. **SMNT**
   - **Command Structure**:  
     ```
     SMNT <directory>
     ```
   - **Response Structure**:  
     ```
     250 Requested file action okay, completed.
     ```
     - **Explanation**: The server acknowledges mounting the file system.

7. **QUIT**
   - **Command Structure**:  
     ```
     QUIT
     ```
   - **Response Structure**:  
     ```
     221 Goodbye.
     ```
     - **Explanation**: The server closes the connection and logs the user out.

8. **REIN**
   - **Command Structure**:  
     ```
     REIN
     ```
   - **Response Structure**:  
     ```
     220 Service ready for new user.
     ```
     - **Explanation**: The server resets the FTP session, typically after a `QUIT` command.

9. **PORT**
   - **Command Structure**:  
     ```
     PORT <host-ip>,<port-number>
     ```
   - **Response Structure**:  
     ```
     200 Command okay.
     ```
     - **Explanation**: The server prepares for active mode file transfer.

10. **PASV**
    - **Command Structure**:  
      ```
      PASV
      ```
    - **Response Structure**:  
      ```
      227 Entering Passive Mode (<ip>,<port1>,<port2>).
      ```
    - **Explanation**: The server prepares for passive mode file transfer, returning the IP and port for the connection.

11. **TYPE**
    - **Command Structure**:  
      ```
      TYPE <type>
      ```
    - **Response Structure**:  
      ```
      200 Type set to <type>.
      ```
    - **Explanation**: The server sets the data transfer type (`A` for ASCII, `I` for binary).

12. **STRU**
    - **Command Structure**:  
      ```
      STRU <structure>
      ```
    - **Response Structure**:  
      ```
      200 Structure set to <structure>.
      ```
    - **Explanation**: The server sets the file structure (`F` for file, `R` for record).

13. **MODE**
    - **Command Structure**:  
      ```
      MODE <mode>
      ```
    - **Response Structure**:  
      ```
      200 Mode set to <mode>.
      ```
    - **Explanation**: The server sets the data transfer mode (`S` for stream, `B` for block).

14. **RETR**
    - **Command Structure**:  
      ```
      RETR <filename>
      ```
    - **Response Structure**:  
      ```
      150 Opening data connection for <filename>.
      226 Transfer complete.
      ```
    - **Explanation**: The server prepares for file download.

15. **STOR**
    - **Command Structure**:  
      ```
      STOR <filename>
      ```
    - **Response Structure**:  
      ```
      150 Opening data connection for <filename>.
      226 Transfer complete.
      ```
    - **Explanation**: The server prepares for file upload.

16. **STOU**
    - **Command Structure**:  
      ```
      STOU
      ```
    - **Response Structure**:  
      ```
      150 Opening data connection for unique file.
      226 Transfer complete.
      ```
    - **Explanation**: The server uploads a file with a unique name.

17. **DELE**
    - **Command Structure**:  
      ```
      DELE <filename>
      ```
    - **Response Structure**:  
      ```
      250 Requested file action okay, completed.
      ```
    - **Explanation**: The server deletes the specified file.

18. **RNFR**
    - **Command Structure**:  
      ```
      RNFR <old-filename>
      ```
    - **Response Structure**:  
      ```
      350 Requested file action pending further information.
      ```
    - **Explanation**: The server prepares for file renaming.

19. **RNTO**
    - **Command Structure**:  
      ```
      RNTO <new-filename>
      ```
    - **Response Structure**:  
      ```
      250 Requested file action okay, completed.
      ```
    - **Explanation**: The server renames the file.

20. **ABOR**
    - **Command Structure**:  
      ```
      ABOR
      ```
    - **Response Structure**:  
      ```
      426 Connection closed; transfer aborted.
      250 Requested file action okay, completed.
      ```
    - **Explanation**: The server aborts the ongoing file transfer.

21. **PWD**
    - **Command Structure**:  
      ```
      PWD
      ```
    - **Response Structure**:  
      ```
      257 "<path>" is the current directory.
      ```
    - **Explanation**: The server returns the current working directory.

22. **LIST**
    - **Command Structure**:  
      ```
      LIST [<directory>]
      ```
    - **Response Structure**:  
      ```
      150 Opening data connection.
      <directory-listing>
      226 Transfer complete.
      ```
    - **Explanation**: The server lists directory contents.

23. **NLST**
    - **Command Structure**:  
      ```
      NLST [<directory>]
      ```
    - **Response Structure**:  
      ```
      150 Opening data connection.
      <file-list>
      226 Transfer complete.
      ```
    - **Explanation**: The server lists filenames in the specified directory.

24. **SITE**
    - **Command Structure**:  
      ```
      SITE <command>
      ```
    - **Response Structure**:  
      ```
      200 Command okay.
      ```
    - **Explanation**: The server executes a site-specific command (varies per server).

25. **HELP**
    - **Command Structure**:  
      ```
      HELP [<command>]
      ```
    - **Response Structure**:  
      ```
      214 Help message, e.g.:
      HELP <command> - Provide details about the specific command.
      ```
    - **Explanation**: The server provides help about a specific FTP command.

26. **NOOP**
    - **Command Structure**:  
      ```
      NOOP
      ```
    - **Response Structure**:  
      ```
      200 Command okay.
      ```
    - **Explanation**: The server acknowledges the command without any action.

---

#### **Extended FTP Commands (RFC 2228, RFC 2428, RFC 3659, etc.)**
27. **AUTH TLS**
    - **Command Structure**:  
      ```
      AUTH TLS
      ```
    - **Response Structure**:  
      ```
      234 Proceed with negotiation
      ```
    - **Explanation**: The server begins TLS negotiation.

28. **EPSV**
    - **Command Structure**:  
      ```
      EPSV
      ```
    - **Response Structure**:  
      ```
      229 Entering Extended Passive Mode (|||<port>|)
      ```
    - **Explanation**: The server prepares for extended passive mode.

29. **EPRT**
    - **Command Structure**:  
      ```
      EPRT |2|<ip>|<port>|
      ```
    - **Response Structure**:  
      ```
      200 Command okay.
      ```
    - **Explanation**: The server prepares for extended active mode.

30. **MLST**
    - **Command Structure**:  
      ```
      MLST <filename>
      ```
    - **Response Structure**:  
      ```
      250 Requested file action okay, completed.
      <file-details>
      ```
    - **Explanation**: The server provides machine-readable file details.

31. **MLSD**
    - **Command Structure**:  
      ```
      MLSD
      ```
    - **Response Structure**:  
      ```
      150 Opening data connection.
      <file-details>
      226 Transfer complete.
      ```
    - **Explanation**: The server provides machine-readable directory listing.

32. **SIZE**
    - **Command Structure**:  
      ```
      SIZE <filename>
      ```
    - **Response Structure**:  
      ```
      213 <size>
      ```
    - **Explanation**: The server returns the size of the file.

33. **MDTM**
    - **Command Structure**:  
      ```
      MDTM <filename>
      ```
    - **Response Structure**:  
      ```
      213 <timestamp>
      ```
    - **Explanation**: The server returns the last modification timestamp of the file.

34. **LANG**
    - **Command Structure**:  
      ```
      LANG <language-code>
      ```
    - **Response Structure**:  
      ```
      200 Command okay.
      ```
    - **Explanation**: The server sets the preferred language.

35. **PROT**
    - **Command Structure**:  
      ```
      PROT <level>
      ```
    - **Response Structure**:  
      ```
      200 Command okay.
      ```
    - **Explanation**: The server sets the data protection level.

36. **PBSZ**
    - **Command Structure**:  
      ```
      PBSZ <size>
      ```
    - **Response Structure**:  
      ```
      200 Command okay.
      ```
    - **Explanation**: The server sets the protection buffer size.

37. **UTF8**
    - **Command Structure**:  
      ```
      UTF8 <on|off>
      ```
    - **Response Structure**:  
      ```
      200 Command okay.
      ```
    - **Explanation**: The server enables or disables UTF-8 support for the session.

38. **SYST**
    - **Command Structure**:  
      ```
      SYST
      ```
    - **Response Structure**:  
      ```
      215 UNIX Type: L8
      ```
    - **Explanation**: The server returns the system type.

39. **FEAT**
    - **Command Structure**:  
      ```
      FEAT
      ```
    - **Response Structure**:  
      ```
      211-Features supported:
      AUTH TLS
      EPSV
      EPRT
      MLST
      MLSD
      SIZE
      MDTM
      LANG
      UTF8
      211 End of feature list
      ```
    - **Explanation**: The server lists the supported features.
