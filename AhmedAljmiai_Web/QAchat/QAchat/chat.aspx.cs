using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Parse;
namespace QAchat
{
    public partial class chat : System.Web.UI.Page
    {
        static string user = "Ahmed";
        static string destenation = "All";
        static string gMessage = "";
        static bool mPrivate = false;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                ParseClient.Initialize("WUXa2xvzApxSNu65oisCysyqWHahMjIVJ1o13TXq", "qi2N6r6EVxgTRHbXZhGBx0otPD1xcOMB4FrKl7y0");
                TextBox1.Text = user;
                loadChatHistory();
            }
            ListBox1.SelectedIndex = ListBox1.Items.Count - 1;
        }
        protected void Page_LoadComplete(object sender, EventArgs e)
        {

        }

        protected async void loadChatHistory()
        {
            ParseQuery<ParseObject> query = ParseObject.GetQuery("ChatMessages");
            IEnumerable<ParseObject> results = await query.FindAsync();
            string MessageBody;
            string From;
            string To;
            bool mChatPrivate;
            string createdAt;
            foreach (ParseObject messages in results)
            {
                try
                {
                    MessageBody = messages.Get<string>("MessageText");
                    From = messages.Get<string>("Id");
                    To = messages.Get<string>("To");
                    mChatPrivate = messages.Get<bool>("Private");
                    createdAt = messages.CreatedAt.ToString();
                    if (mChatPrivate && To != user)
                    {
                        continue;
                    }
                    else if (From == user)
                    {
                        if (mChatPrivate)
                        {
                            ListBox1.Items.Add("Me [Private] to " + To + " [" + createdAt + "] :" + MessageBody);
                        }
                        else
                        {
                            ListBox1.Items.Add("Me [" + createdAt + "] :" + MessageBody);
                        }
                    }
                    else if (To == user)
                    {
                        if (mChatPrivate)
                        {
                            ListBox1.Items.Add(From + " [Private] [" + createdAt + "] :" + MessageBody);
                        }
                        else
                        {
                            ListBox1.Items.Add(From + " [" + createdAt + "] :" + MessageBody);
                        }
                    }
                    else
                    {
                        ListBox1.Items.Add(From + " [" + createdAt + "] :" + MessageBody);
                    }
                }
                catch (Exception ex)
                {
                    //MessageBox.Show(ex.ToString());
                    continue;
                }
            }
        }





        protected void Button1_Click(object sender, EventArgs e)
        {
            gMessage = TextBox3.Text;
            if (gMessage != "")
            {
                if (!mPrivate)
                    ListBox1.Items.Add("Me [" + DateTime.Now.ToString() + "] : " + gMessage);
                else if (TextBox2.Text != "")
                {
                    ListBox1.Items.Add("Me [Private] to " + destenation + " [" + DateTime.Now.ToString() + "] : " + gMessage);
                }
                else
                {
                    Response.Write("<script LANGUAGE='JavaScript' >alert('Please enter destenation ID!')</script>");
                    return;
                }
                gMessage = TextBox3.Text.ToString();
                sendMessage(user, destenation, gMessage, mPrivate);
                ListBox1.SelectedIndex = ListBox1.Items.Count - 1;
                ListBox1.SelectedIndex = -1;
                TextBox3.Text = "";
            }
            Response.Write("<script LANGUAGE='JavaScript' >alert('Please enter a message to send!')</script>");

        }

        private async void sendMessage(string source, string destenation, string gMessage, bool mPrivate)
        {
            ParseObject chatMessages = new ParseObject("ChatMessages");
            chatMessages["Id"] = source;
            chatMessages["To"] = destenation;
            chatMessages["Private"] = mPrivate;
            chatMessages["MessageText"] = gMessage;
            await chatMessages.SaveAsync();
        }

        protected void Private_CheckedChanged(object sender, EventArgs e)
        {

            mPrivate = Private.Checked;
            if (!mPrivate)
                destenation = "All";
        }

        protected void TextBox1_TextChanged(object sender, EventArgs e)
        {
            user = TextBox1.Text;
        }

        protected void TextBox2_TextChanged(object sender, EventArgs e)
        {

        }
    }
}