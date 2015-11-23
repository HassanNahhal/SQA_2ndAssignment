Parse.initialize("WUXa2xvzApxSNu65oisCysyqWHahMjIVJ1o13TXq", "Bn4edMGssPUHffai7LrAdyUNCaQRvdJgE6e5WdH1");

$(document).ready(function(){    
    $("#sendChat").on('click',function(e){
        e.preventDefault();
        sendMessage();
    });
    setInterval(getMessage, 1000);
});

function checkBlankValue(){
    
    if($("#chatID").val() == ""){
        alert("Please enter the 'From ID'.");
        $("#chatID").focus();
        return false;
    }
        if($("#toID").val() == ""){
        alert("Please enter the 'To ID'."); 
        $("#toID").focus();
        return false;
    }
    if($("#inputText").val() == ""){
        alert("Please enter the 'Message'.");
        $("#inputText").focus();
        return false;
    }
    return true;
}

function sendMessage(){	
    
    if(!checkBlankValue())
        return;

    var ChatObject = Parse.Object.extend("ChatMessages");
    var chatObject = new ChatObject();        

    var chatID = $("#chatID").val();        
    var toID = $("#toID").val();          
    var chatText = $("#inputText").val(); 
    var chatPrivate = false;

    if($("#private").is(":checked"))
        chatPrivate = true;

    chatObject.set("Id", chatID);
    chatObject.set("MessageText", chatText);
    chatObject.set("Private", chatPrivate);
    chatObject.set("To", toID);

    chatObject.save(null, {
        success: function(chatObject) {           
            getMessage();
        },
        error: function(chatObject, error) {
            $(".error").show();
        }
    });	
}

// Define the meaningful constant number to display the chatting list
// Deu to it is more efficient to limited line in stead of all history of chatting list.
var globalMaximumQueryList = 20;
var glogalMaximumDisplayChattingList = 10;

function getMessage(){
    
    var myID = $("#chatID").val(); 
    var listCount = 0;
    
    var GetChat = Parse.Object.extend("ChatMessages");
    var getChat = new Parse.Query(GetChat);
    
    getChat.descending("createdAt");
    getChat.limit(globalMaximumQueryList);
        
    getChat.find({
        success: function(results){
            
            $("#chatList").html("");
            
            $(results).each(function(i,e){

                var query = e.toJSON();
                
                var sendID = encodeHTML(query.Id);
                var toID = encodeHTML(query.To);
                var txtMsg = encodeHTML(query.MessageText);
                var txtPrivate = "";  
                var chatLine = "";
                var tempLine = "";
                
                if(query.Private)
                    txtPrivate = " {Private}";
                
                tempLine = "<strong>" + sendID + txtPrivate + " - </strong> " + txtMsg + "<br>"; 
                
                if(query.Private){
                    if(myID == toID || myID == sendID){
                        chatLine = tempLine; 
                        listCount ++;
                    }
                }else{
                    chatLine = tempLine; 
                    listCount ++;
                }
                
                if(listCount <= glogalMaximumDisplayChattingList){
                    $("#chatList").append(chatLine);
                }else{ return;}                
            });  
        },
        error: function(results, error){
            console.log(error.message);
        }
    });
}  

function encodeHTML(inputVar)
{
    if(inputVar != undefined && inputVar != null){
        inputVar = inputVar.replace(/&/g, "&amp;");
        inputVar = inputVar.replace(/>/g, "&gt;");
        inputVar = inputVar.replace(/</g, "&lt;");
        inputVar = inputVar.replace(/"/g, "&quot;");
        inputVar = inputVar.replace(/'/g, "&#039;");
    }
    return inputVar;
}