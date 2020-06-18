// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


// global variables
const commentFormID = 'compose-comment-form';
const deleteCommentsID = 'delete-comments';
const loginID = 'login';
const styleNone = 'none';
const commentFieldID = 'comments-field';

/**
 * Loads sidebar navigation from sidebar.html and underlines current page
 */
window.onload = function() {
  $(".sidebar").load("sidebar.html", function() {
    var currentPageId = window.location.pathname.slice(1, -5);
    document.getElementById(currentPageId).style.textDecoration = "underline";
  })
  checkLogin();
  displayComments();
  fetchBlobstoreURL();
}

async function checkLogin() {
  const response = await fetch('/login');
  const messageArr = await response.json();


  if (messageArr[0] == 'true') {
    // TODO: display email address in message
    document.getElementById(loginID).innerHTML = "<p>You're logged in as " 
         + messageArr[2] + ". Logout <a href=\"" + messageArr[1] + "\">here</a>.</p>";
  } else {
    document.getElementById(loginID).innerHTML = "<p>Login <a href=\"" + messageArr[1]
         + "\">here</a> to share a comment.</p>";
    document.getElementById(commentFormID).style.display = styleNone;
    document.getElementById(deleteCommentsID).style.display = styleNone;
  }
}

async function displayComments() {
  var numComments = document.getElementById("num-comments-selector").value;
  if (numComments == null) {
    // show 5 comments by default
    numComments = 5;
  }
  const response = await fetch('/data?numComments=' + numComments);
  const messageArr = await response.json();

  var output = [];

  messageArr.forEach(function(comment) {
    var dateString = new Date(parseInt(comment.timestamp)).toDateString();
    var container = `<div class='comment'>
    <div class="username">${comment.username}</div>
    <div class="date">Posted on ${dateString}</div>
    <p>${comment.message}</p>`;
    if (comment.imageURL != null) {
      container += `<div class="comment-img-crop"><img src="${comment.imageURL}"></div>`;
    }
    container += `</div>`;
    output.push(container);
  });
  
  document.getElementById(commentFieldID).innerHTML = output.join("");
}

/** Grab the BlobStore URL for image upload and change the onsubmit action */
function fetchBlobstoreURL() {
  fetch('/blobstore-upload-URL')
    .then((response) => response.text())
    .then((imageUploadURL) => {
      const form = document.getElementById(commentFormID);
      form.action = imageUploadURL;
    });
}
