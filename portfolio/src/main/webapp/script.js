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

/**
 * Loads sidebar navigation from sidebar.html and underlines current page
 */

window.onload = function() {
  $(".sidebar").load("sidebar.html", function() {
    var currentPageId = window.location.pathname.slice(1, -5);
    document.getElementById(currentPageId).style.textDecoration = "underline";
  })
}

async function checkLogin() {
  const response = await fetch('/login');
  const messageArr = await response.json();
  return messageArr;
}

async function displayComments() {
  var checkLoginArr = await checkLogin();
  if (checkLoginArr[0] == 'true') {
    var numComments = document.getElementById("num-comments-selector").value;
    const response = await fetch('/data?numComments=' + numComments);
    const messageArr = await response.json();

    // Split messageArr into paragraph elements
    var output = messageArr.map(str => "<p>" + str.message + "</p>");
    document.getElementById('comments-field').innerHTML = output.join("") + "<p>Logout <a href=\"" + checkLoginArr[1] + "\">here</a>.</p>";
  } else {
    console.log("Oopsie you're logged out!");
    document.getElementById('comments-field').innerHTML = "<p>Login <a href=\"" + checkLoginArr[1] + "\">here</a>.</p>"
  }
}