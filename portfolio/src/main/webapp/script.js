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

async function hello() {
  const response = await fetch('/data');
  const greeting = await response.text();
  document.getElementById('button-text').innerHTML = greeting;
}