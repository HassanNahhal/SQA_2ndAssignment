    var ChatObject = Parse.Object.extend("ChatMessages");
    var chatObject = new ChatObject();
	
	function sendMessage(){	
	
		var chatID = document.getElementById('chatID').value;
		var toID = document.getElementById('toID').value;
		var chatText = document.getElementById('inputText').value;
		var chatPrivate = false;
		
		if(document.getElementById('private').checked){
			chatPrivate = true;
		}
		
		var $chatList = $("#chatlist");
	
		chatObject.set("Id", chatID);
		chatObject.set("MessageText", chatText);
		chatObject.set("Private", chatPrivate);
		chatObject.set("To", toID);
		
		  chatObject.save(null, {
		  success: function(chatObject) {
			$(".success").show();
			$chatList.html("[ " + chatID + " ] " + chatText + " id: " + chatObject.id);
		  },
		  error: function(model, error) {
			$(".error").show();
		  }
		});	
	}
	
	function getMessage(){
		
		//var ReceiveObject = Parse.Object.extend("ChatMessages");
		var receiveObject = new Parse.Query(ChatObject);

		receiveObject.get("fQ8DyDp37u",{
			  success: function(receiveObject) {
				// The object was retrieved successfully.
				//alert("retrieved successfully");
				alert(receiveObject.id);
			  },
			  error: function(object, error) {
				// The object was not retrieved successfully.
				// error is a Parse.Error with an error code and message.
			  }
		});
	}
	
	window.onload = function(){
		document.getElementById('sendtext').onclick = function(){
			sendMessage();
		}
		
		document.getElementById('receivetext').onclick = function(){
			getMessage();
		}		
	}	