async function startup(){
    try {
    const response = await fetch('http://localhost:8080/files', {
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
    const data = await response.json().then(files => displayFiles(files));
    //console.log(data)
    //return data;
  } catch (error) {
    console.error('Error:', error);
   // throw error; // Handle error appropriately
  }
}

async function download(s){
    
    try {
      const response =await fetch(s, {
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
      const contentDisposition = response.headers.get('Content-Disposition');
      let filename = 'file.pdf';
      if (contentDisposition) {
          const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
          const matches = filenameRegex.exec(contentDisposition);
          if (matches != null && matches[1]) {
              filename = matches[1].replace(/['"]/g, '');
          }
      }
      // Return blob from response
      return response.blob().then(blob => {
          // Create an object URL for the blob
          const url = window.URL.createObjectURL(blob);
          // Create a link element
          const a = document.createElement('a');
          // Set the href attribute to the object URL
          a.href = url;
          // Set the download attribute with the filename
          a.download = filename;
          // Append the link to the body
          document.body.appendChild(a);
          // Trigger a click event on the link
          a.click();
          // Cleanup: remove the link and revoke the object URL
          window.URL.revokeObjectURL(url);
          document.body.removeChild(a);
      });
  } catch (error) {
    console.error('Error:', error);
   // throw error; // Handle error appropriately
  }
  
}  

// Function to display the list of files in the HTML
function displayFiles(files) {
  const fileListContainer = document.getElementById('filesshow');

  // Clear previous content
  fileListContainer.innerHTML = '';

  // Iterate over the list of files and create HTML elements to display each file
  files.forEach(file => {
      const fileElement = document.createElement('div');
      fileElement.classList.add('file'); // Add class for styling
      fileElement.innerHTML = `
          <p><strong>Name:</strong> ${file.name}</p>
          <p><strong>Type:</strong> ${file.type}</p>
          <p><strong>Size:</strong> ${file.size} bytes</p>
          <button onclick="download('${file.url}')">Download</button>
      `;
      fileListContainer.appendChild(fileElement);
  });
}



startup();


// Fetch files when the page loads
//fetchFiles();

//console.log(localStorage.getItem("Token"))
//console.log(localStorage.getItem("Token").role)
//document.getElementById("12").innerHTML=localStorage.getItem("Token");