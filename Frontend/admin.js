async function testFunction(){
    
    try {
      const response = await fetch('http://localhost:8080/welcomeAdmin', {
        method: 'GET', // or POST, PUT, DELETE, etc.
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem("Token"),
          'Content-Type': 'application/json'
        },
        // Add body if needed for POST, PUT, etc.
      });
      
      // Check if response is successful
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
  
      // Parse JSON response
      const data = await response.text();
      console.log(data)
      return data;
    } catch (error) {
      console.error('Error:', error);
     // throw error; // Handle error appropriately
    }
  
}


document.getElementById('uploadButton').addEventListener('click', async function() {
  const fileInput = document.getElementById('fileInput');
  const file = fileInput.files[0];

 
  if (!file) {
      console.error('Please select a file.');
      return;
  }

  const formData = new FormData();
  formData.append('file', file);

  try {
      const response = await fetch('http://localhost:8080/uploadsinglefile', {
          method: 'POST',
          body: formData,
          headers: {
            'Authorization': 'Bearer ' + localStorage.getItem("Token"),
            //'Content-Type': 'application/json'
          }
      });

      if (!response.ok) {
          throw new Error('Upload failed');
      }
      alert("Uploaded successfully!");
      window.location.href="http://127.0.0.1:5500/admin.html";

      const responseData = await response.text();
      console.log(responseData); // Handle successful response
  } catch (error) {
      console.error('Error uploading file:', error.message);
  }
});