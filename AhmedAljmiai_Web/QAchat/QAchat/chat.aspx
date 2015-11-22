<%@ Page Language="C#" Async="true"  AutoEventWireup="true" CodeBehind="chat.aspx.cs" Inherits="QAchat.chat" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body style="text-align: center">
    <form id="form1" runat="server"> 
   <asp:ScriptManager ID="ScriptManager1" runat="server" />
 <div>
    
        Source ID:
        <asp:TextBox ID="TextBox1" runat="server" OnTextChanged="TextBox1_TextChanged"></asp:TextBox>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Destenation ID:
        <asp:TextBox ID="TextBox2" runat="server" OnTextChanged="TextBox2_TextChanged"></asp:TextBox>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Private:
        <asp:CheckBox ID="Private" runat="server" OnCheckedChanged="Private_CheckedChanged" />
        <br />
        <br />
     <asp:Timer ID="Timer1" runat="server" Interval="3600" OnTick="Timer1_Tick">
</asp:Timer>
                <asp:UpdatePanel ID="UpdatePanel3" runat="server" UpdateMode="Conditional">
                <ContentTemplate>
                <asp:ListBox ID="ListBox1" runat="server" Height="154px" Width="100%" ></asp:ListBox>
                    <br />
                    <br />
                </ContentTemplate>
                <Triggers>
                    <asp:AsyncPostBackTrigger ControlID="Timer1" EventName="Tick" />
                </Triggers>
            </asp:UpdatePanel>

        <asp:TextBox ID="TextBox3" runat="server" Width="90%"></asp:TextBox>
        <asp:Button ID="Button1" runat="server" OnClick="Button1_Click" Text="Send" />
    
    </div>
    </form>
</body>
</html>
