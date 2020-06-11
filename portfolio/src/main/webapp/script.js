window.onload = function() {
  $(".sidebar").load("sidebar.html", function() {
    var currentPageId = window.location.pathname.slice(1, -5);
    document.getElementById(currentPageId).style.textDecoration = "underline";
  })
}
