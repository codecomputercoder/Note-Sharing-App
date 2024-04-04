function login(){

    fetch('http://localhost:8080/genToken', {
        method: 'POST', // Change method to POST
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          "userName": "admin",
          "password": "pass"
        })
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        console.log(response);
        return response.json(); // Expecting a text response, not JSON
      })
      .then(token => {
        console.log('Bearer Token:', token);

        if(token.role=="admin"){
            window.location.href="http://127.0.0.1:5500/user.html";
        }
        if(token.role=="user"){
            window.location.href="http://127.0.0.1:5500/user.html";
        }
        localStorage.setItem("Token", token.token);
        localStorage.setItem("Role", token.role);
      })
      .catch(error => {
        console.error('Error:', error);
      });

    console.log("hello");
      
}

