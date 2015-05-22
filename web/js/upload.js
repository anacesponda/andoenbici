$(function() {
    // Span
var span = document.getElementsByClassName('upload-path');
// Button
var uploader = document.getElementsByName('upload');
// On change
for( item in uploader ) {
  // Detect changes
  uploader[item].onchange = function() {
    // Echo filename in span
    span[0].innerHTML = this.files[0].name;
  }
}
   
});



