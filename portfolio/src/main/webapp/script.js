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
 * Loads sidebar navigation from sidebar.html
 */
window.onload = function() {
  $(".sidebar").load("sidebar.html");
};

/**
 * Adds a random team name to the page.
 */
function addRandomTeamName() {
  const teamNames =
      ["Wow, she's really good!", "Harambe", "Kevin and the Zits", "*villager noises*", "Very cool, thanks Kanye", "Ligma Kappa"];

  // Pick a random name.
  const name = teamNames[Math.floor(Math.random() * teamNames.length)];

  // Add it to the page.
  const nameContainer = document.getElementById('name-container');
  nameContainer.innerText = name;
}
