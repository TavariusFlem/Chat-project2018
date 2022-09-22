// IMPORTS

class Buddy
    {
//============================ DATA MEMBERS =====================================
    String          buddysUsername;
    boolean         isOnline;
    ChatDialog      chatDialog;
    //messageQueue
//------------------------------
    Buddy(String buddysUsername, boolean isOnline)
        {
        this.buddysUsername = buddysUsername;
        this.isOnline = isOnline;
        }
/*==================================================================================
====================================================================================
==================================================================================*/
    public String toString()
        {
        String buddyUsernameString;
        if(isOnline)
            {
            //buddyUsernameString = "<FONT COLOR = \"GREEN\">" + buddysUsername + "</FONT>";
            buddyUsernameString = buddysUsername;
            return "*" + buddyUsernameString;
            }
        else
            {
            //buddyUsernameString = "<FONT COLOR = \"RED\">" + buddysUsername + "</FONT>";
            buddyUsernameString = buddysUsername;
            return buddyUsernameString;
            }
        }
    }