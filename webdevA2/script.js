function showPage(topic) {
  // Hide all topic divs
  const topics = document.querySelectorAll('#main-content > div');
  for(let topicDiv of topics) {
    topicDiv.style.display = 'none';
    topicDiv.style.transform = 'translateX(-100%)'; // Add this line for the transition effect
  }

  // Show the selected topic div with transition
  const selectedTopic = document.getElementById(topic);
  selectedTopic.style.display = 'block';
  selectedTopic.style.transform = 'translateX(0%)'; // Add this line for the transition effect

  // Remove the "vibrate" class from all images
  const images = document.querySelectorAll('img');
  for(let image of images) {
    image.classList.remove('vibrate');
  }

  // Add the "vibrate" class to images under topics that are not "game"
  if (topic !== 'game') {
    const topicImages = selectedTopic.querySelectorAll('img');
    for(let image of topicImages) {
      image.classList.add('vibrate');
    }
  }
}

document.addEventListener('DOMContentLoaded', function () {
  // Topics
  document.querySelectorAll("#menu a")[0].addEventListener("click", function(){showPage("breeds");});
  document.querySelectorAll("#menu a")[1].addEventListener("click", function(){showPage("adoption");});
  document.querySelectorAll("#menu a")[2].addEventListener("click", function(){showPage("fun");});
  document.querySelectorAll("#menu a")[3].addEventListener("click", function(){showPage("game");});

  // Breeds
  const breedCards = document.querySelectorAll('.breed-card');
  for(let breedCard of breedCards) {
    breedCard.addEventListener('click', showContent);
  }

  // Adopt
  const adoptCards = document.querySelectorAll('.clickable-card');
  for (let adoptCard of adoptCards) {
    const icon = adoptCard.querySelector('.icon');  
    icon.addEventListener('click', createClickListener(adoptCard));
  }  

  document.querySelector("#playButton").addEventListener("click", function(){startGame();});
});

// Breeds
function showContent(event) {
  const breedCard = event.currentTarget;
  const h3 = breedCard.querySelector('h3');
  const p = breedCard.querySelector('p');

  fadeIn(h3);
  fadeIn(p);
}

function fadeIn(element) {
  element.style.opacity = 0;
  element.style.display = 'block';

  let opacity = 0;
  const fadeInInterval = setInterval(function () {
    opacity += 0.1;
    element.style.opacity = opacity;

    if (opacity >= 1) {
      clearInterval(fadeInInterval);
    }
  }, 100);
}

// Adopt
function slideIn(element) {
  element.style.display = 'block';
  element.style.transform = 'translateX(-100%)'; // Start from the left (slide in position)

  var distance = 100; // Start position for slide-in animation (100%)
  var slideInInterval = setInterval(function () {
    distance -= 2; // Decrease the distance to slide in (from 100% to 0%)
    element.style.transform = 'translateX(-' + distance + '%)';

    if (distance <= 0) {
      clearInterval(slideInInterval);
    }
  }, 20); // Lowered interval (20) for smoother animation
}

function createClickListener(adoptCard) {
  return function () {
    const h3 = adoptCard.querySelector('h3');
    const p = adoptCard.querySelector('p');
    const ul = adoptCard.querySelector('ul');

    slideIn(h3);
    slideIn(p);
    slideIn(ul);
  };
}

let timer;
let score = 0;
let isGameStarted = false;

function startGame() {
  const cat = document.getElementById('cat');
  const timerElement = document.getElementById('timer');
  const scoreElement = document.getElementById('score');

  if (isGameStarted) {
    return;
  }

  // Reset score and timer
  score = 0;
  timerElement.textContent = 'Time: 60';
  scoreElement.textContent = 'Score: ' + score;

  // Set click event on cat image
  cat.addEventListener('click', increaseScore);

  // Start the game
  isGameStarted = true;
  startTimer();
  moveCat(cat);
}

function moveCat(cat) {
  var containerSize = document.querySelector('.cat-container').offsetWidth;
  var catSize = cat.offsetWidth;
  var maxX = containerSize - catSize;
  var maxY = containerSize - catSize;

  // Move the cat randomly within the container
  var randomX = Math.floor(Math.random() * maxX);
  var randomY = Math.floor(Math.random() * maxY);

  cat.style.left = randomX + 'px';
  cat.style.top = randomY + 'px';

  // Wait for a short duration and move again
  setTimeout(function () {
    if (isGameStarted) {
      moveCat(cat);
    }
  }, 800);
}

function startTimer() {
  var timerElement = document.getElementById('timer');
  var timeLeft = 60;
  timer = setInterval(function () {
    timeLeft--;
    timerElement.textContent = 'Time: ' + timeLeft;

    if (timeLeft == 0) {
      endGame();
    }
  }, 1000);
}

function increaseScore() {
  if (isGameStarted) {
    score++;
    const scoreElement = document.getElementById('score');
    scoreElement.textContent = 'Score: ' + score;
  }
}

function endGame() {
  const cat = document.getElementById('cat');
  const timerElement = document.getElementById('timer');

  // Stop the game
  clearInterval(timer);
  isGameStarted = false;

  // Remove click event on cat image
  cat.removeEventListener('click', increaseScore);

  // Display final score
  timerElement.textContent = 'Time: 0\nFinal Score: ' + score;
}