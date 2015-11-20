Parse.initialize("WUXa2xvzApxSNu65oisCysyqWHahMjIVJ1o13TXq", "Bn4edMGssPUHffai7LrAdyUNCaQRvdJgE6e5WdH1");

$(document).ready(function(){    
    $("#sendChat").on('click',function(e){
        e.preventDefault();
        sendMessage();
    });
    setInterval(getMessage, 1000);
});

function sendMessage(){	

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

function getMessage(){
    
    var myID = $("#chatID").val(); 
    var listCount = 0;
    
    var GetChat = Parse.Object.extend("ChatMessages");
    var getChat = new Parse.Query(GetChat);
    
    getChat.descending("createdAt");
    getChat.limit(20);
        
    getChat.find({
        success: function(results){
            
            $("#chatList").html("");
            
            $(results).each(function(i,e){

                var q = e.toJSON();
                
                var sendID = encodeHTML(q.Id);
                var toID = encodeHTML(q.To);
                var txtMsg = encodeHTML(q.MessageText);
                var txtPrivate = "";  
                var chatLine = "";
                var tempLine = "";
                
                if(q.Private)
                    txtPrivate = " {Private}";
                
                tempLine = "<strong>" + sendID + txtPrivate + " - </strong> " + txtMsg + "<br>"; 
                
                if(q.Private){
                    if(myID == toID || myID == sendID){
                        chatLine = tempLine; 
                        listCount ++;
                    }
                }else{
                    chatLine = tempLine; 
                    listCount ++;
                }
                
                if(listCount <= 10){
                    $("#chatList").append(chatLine);
                }                
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