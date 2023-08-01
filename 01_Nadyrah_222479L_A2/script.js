function loadPage(topic) {
    // Get the main content element
    const mainContent = document.getElementById('main-content');
  
    // Clear the current content
    mainContent.innerHTML = '';
  
    // Load content based on the selected topic
    switch (topic) {
        case 'breeds':
            mainContent.innerHTML = `
              <h2>Cat Breeds</h2>
              <div class="breeds-container">
                <div class="breed-card">
                  <img src="images/persian.jpg" alt="Persian Cat">
                  <h3>Persian</h3>
                  <p>The Persian cat is one of the most popular cat breeds. Known for its long and luxurious coat, the Persian is a calm and gentle companion. They have a sweet temperament and enjoy a peaceful and quiet environment. Persians require regular grooming to keep their coat healthy and free from matting.</p>
                </div>
                <div class="breed-card">
                  <img src="images/maine-coon.jpg" alt="Maine Coon Cat">
                  <h3>Maine Coon</h3>
                  <p>The Maine Coon is another beloved cat breed. It is one of the largest domestic cat breeds and is known for its striking appearance and friendly nature. Maine Coons are intelligent and social cats that get along well with children and other pets. They have a thick, shaggy coat that requires regular brushing.</p>
                </div>
                <div class="breed-card">
                  <img src="images/bengal.jpg" alt="Bengal Cat">
                  <h3>Bengal</h3>
                  <p>The Bengal cat is a beautiful and energetic breed that captures attention with its distinctive coat pattern reminiscent of a leopard. Bengals are highly active and playful cats that enjoy interactive toys and activities. They are known for their intelligence and curiosity. Bengal cats require mental and physical stimulation to thrive.</p>
                </div>
              </div>
            `;
            break;
            case 'care':
                mainContent.innerHTML = `
                  <h2>Cat Care and Health</h2>
                  <div class="care-container">
                    <h3>Feeding</h3>
                    <p>Feeding your cat a balanced and appropriate diet is crucial for their overall health. Cats are obligate carnivores, so their diet should primarily consist of high-quality animal protein. It's important to choose cat food that meets their nutritional needs and avoid overfeeding to prevent obesity.</p>
                    <h3>Grooming</h3>
                    <p>Grooming plays a vital role in keeping your cat's coat and skin healthy. Long-haired cats, such as Persians, require daily brushing to prevent matting. Short-haired cats may need brushing once a week. Additionally, regular nail trims, ear cleaning, and dental care are essential parts of cat grooming.</p>
                    <h3>Litter Box Training</h3>
                    <p>Litter box training is crucial for maintaining good hygiene and ensuring your cat's comfort. Place the litter box in a quiet and accessible location. Provide a litter substrate that your cat prefers, and keep the box clean by scooping it daily. Aim for one litter box per cat plus an extra.</p>
                    <h3>Vaccinations</h3>
                    <p>Vaccinations protect cats from various infectious diseases. Common vaccinations include those for feline viral rhinotracheitis, calicivirus, panleukopenia (FVRCP), and rabies. Consult with your veterinarian to determine the appropriate vaccination schedule and ensure your cat is up to date.</p>
                    <h3>Common Health Issues</h3>
                    <p>Cats can experience various health issues, including dental problems, urinary tract infections, and gastrointestinal disorders. Regular veterinary check-ups are important for early detection and treatment. Watch for signs of illness, such as changes in appetite, litter box habits, or behavior, and seek veterinary care if necessary.</p>
                    <h3>Creating a Stimulating Environment</h3>
                    <p>Enriching your cat's environment is essential for their mental and physical well-being. Provide scratching posts, interactive toys, and vertical spaces like cat trees or shelves. Offer opportunities for play, exercise, and mental stimulation to prevent boredom and promote a happy, healthy cat.</p>
                  </div>
                `;
                break;
                case 'adoption':
                    mainContent.innerHTML = `
                      <h2>Adoption and Rescue</h2>
                      <div class="adoption-container">
                      <p>Promote cat adoption and provide information about local shelters, rescue organizations, and adoption events...</p>
                      <h3>Why Adoption and Rescue are Important</h3>
                      <p>Adopting a cat from a shelter or rescue organization is a compassionate and responsible choice. Here are some reasons why adoption and rescue are important:</p>
                      <ul>
                        <li><strong>Saving Lives:</strong> By adopting a cat, you provide a loving home to an animal in need. Many cats in shelters are looking for a second chance at a happy life.</li>
                        <li><strong>Reducing Overpopulation:</strong> Adoption helps to address the issue of cat overpopulation. By choosing adoption, you actively contribute to reducing the number of stray and homeless cats.</li>
                        <li><strong>Supporting Ethical Practices:</strong> Reputable shelters and rescue organizations prioritize the welfare of animals. When you adopt, you support organizations that work to ensure the well-being of cats and promote responsible pet ownership.</li>
                        <li><strong>Health and Behavior:</strong> Cats available for adoption are often spayed or neutered, vaccinated, and given necessary medical care. Additionally, shelters and rescues provide valuable insights into a cat's behavior and temperament, helping you find a cat that fits your lifestyle.</li>
                      </ul>
                      <h3>Adopt, Don't Shop</h3>
                      <p>The motto "adopt, don't shop" encourages people to consider adoption as the preferred option when bringing a new cat into their lives. By adopting, you not only provide a loving home to a cat but also help create a more compassionate society that values the well-being of all animals.</p>
                      <p>Remember, there are many wonderful cats waiting to be adopted in shelters and rescue organizations. Give them a chance and experience the joy of saving a life!</p>
                    <h3>Adopting in Singapore</h3>
                        <p>Singapore has several reputable animal shelters and rescue organizations dedicated to cat adoption. Here are a few notable ones:</p>
                        <ul>
                          <li><strong>Society for the Prevention of Cruelty to Animals (SPCA)</strong>: The SPCA in Singapore actively promotes the adoption of cats and other animals. Visit their website to view available cats and learn more about the adoption process.</li>
                          <li><strong>Cat Welfare Society (CWS)</strong>: CWS focuses on improving the welfare of community cats in Singapore. They facilitate adoptions and offer trap-neuter-release programs.</li>
                          <li><strong>Love Kuching Project</strong>: Love Kuching Project is a cat rescue group that provides shelter, veterinary care, and adoption services. They specialize in rehabilitating sick and injured cats.</li>
                        </ul>
                        <p>These organizations hold regular adoption drives and events where you can meet cats looking for their forever homes. Adopting a cat not only gives them a second chance at a happy life but also helps to reduce the number of stray cats in Singapore.</p>
                      </div>
                    `;
                    break;
                    case 'behavior':
                        mainContent.innerHTML = `
                          <h2>Behaviour and Training</h2>
                          <div class="behavior-container">
                            <h3>Socialization</h3>
                            <p>Socializing your cat is crucial for their well-being and adaptability in Singapore's urban environment. Introduce your cat to various people, sounds, and environments gradually. Positive reinforcement techniques can help them become comfortable and confident.</p>
                            <h3>Litter Training</h3>
                            <p>Litter training your cat is essential for maintaining hygiene in your home. Provide a litter box with appropriate litter material, preferably unscented. Place the litter box in a quiet and accessible location. Be patient and reward your cat for using the litter box correctly.</p>
                            <h3>Scratching Behavior</h3>
                            <p>Redirecting your cat's scratching behavior is crucial to protect your furniture and belongings. Provide suitable scratching posts or boards that are sturdy and tall. Encourage your cat to use them by placing them near areas they like to scratch and using positive reinforcement.</p>
                            <h3>Introducing a New Cat</h3>
                            <p>Introducing a new cat to your household requires careful management. Gradually introduce the new cat to the resident cat by swapping scents and gradually increasing supervised interactions. Provide separate resources and ensure each cat has their safe space.</p>
                            <h3>Addressing Behavior Problems</h3>
                            <p>Addressing behavior problems in Singapore may require a holistic approach. Consult with a veterinarian or a professional cat behaviorist who understands the local environment. They can provide guidance on common behavior problems like aggression, inappropriate elimination, or excessive vocalization.</p>
                          </div>
                        `;
                        break;
                        case 'products':
                            mainContent.innerHTML = `
                              <h2>Cat Products and Reviews (Singapore)</h2>
                              <div class="products-container">
                                <div class="product-card">
                                  <img src="images/cat-food.jpg" alt="Cat Food">
                                  <h3>Cat Food</h3>
                                  <p>Choosing the right cat food is essential for your feline friend's health. In Singapore, popular cat food brands include ABC Cat Food, XYZ Premium Cat Cuisine, and PQR Natural Cat Delights. Ensure you provide a balanced diet that meets your cat's nutritional needs.</p>
                                  <p>Required items for obtaining cat food in Singapore: pet food containers, feeding bowls, and airtight storage for opened cat food packets.</p>
                                </div>
                                <div class="product-card">
                                  <img src="images/cat-toys.jpg" alt="Cat Toys">
                                  <h3>Cat Toys</h3>
                                  <p>Keeping your cat entertained and mentally stimulated is important. In Singapore, popular cat toys include interactive treat puzzles, feather wands, and catnip-filled toys. These toys provide exercise and help prevent boredom.</p>
                                  <p>Required items for cat toys in Singapore: scratching posts, cat tunnels, and interactive toys for playtime.</p>
                                </div>
                                <div class="product-card">
                                  <img src="images/cat-litter.jpg" alt="Cat Litter">
                                  <h3>Cat Litter</h3>
                                  <p>Choosing the right cat litter is crucial for maintaining a clean and odor-free environment. In Singapore, commonly used cat litter brands include UVW Odor-Control Litter, LMN Natural Wood Pellets, and XYZ Clumping Litter. Consider factors such as clumping ability, odor control, and your cat's preference.</p>
                                  <p>Required items for cat litter in Singapore: litter boxes, litter scoops, and odor-neutralizing sprays.</p>
                                </div>
                              </div>
                            `;
                            break;
                            case 'fun':
                                mainContent.innerHTML = `
                                  <h2>Fun and Entertainment</h2>
                                  <div class="fun-container">
                                    <h3>Articles</h3>
                                    <p>Engaging articles about cats, their behavior, and interesting facts...</p>
                                    
                                    <h3>Videos</h3>
                                    <div class="video-container">
                                    <p>Watch adorable and funny cat videos from Singapore. Enjoy heartwarming stories, cute antics, and viral clips that will brighten your day.</p>
                                      <iframe width="560" height="315" src="https://www.youtube.com/embed/video1" frameborder="0" allowfullscreen></iframe>
                                      <iframe width="560" height="315" src="https://www.youtube.com/embed/video2" frameborder="0" allowfullscreen></iframe>
                                      <iframe width="560" height="315" src="https://www.youtube.com/embed/video3" frameborder="0" allowfullscreen></iframe>
                                    </div>
                                    
                                    <h3>Quizzes</h3>
                                    <p>Test your knowledge with interactive cat-themed quizzes...</p>
                                  </div>
                                `;
                                break;
                                case 'community':
                                    mainContent.innerHTML = `
                                      <h2>Community and Forums</h2>
                                      <div class="community-container">
                                        <p>Connect with fellow cat lovers, share experiences, and seek advice in Singapore's vibrant cat community. Here are some popular cat forums and resources in Singapore:</p>
                                        <ul>
                                          <li><a href="https://www.catwelfare.org/" target="_blank">Cat Welfare Society</a>: A non-profit organization dedicated to improving the lives of cats in Singapore. They provide a wealth of information about cat adoption, care, and community initiatives.</li>
                                          <li><a href="https://www.singaporecatclub.com/" target="_blank">Singapore Cat Club</a>: An online community for cat owners and enthusiasts in Singapore. They have forums where you can connect with other cat lovers, share stories, and ask for advice.</li>
                                          <li><a href="https://www.facebook.com/groups/sgcats/" target="_blank">SG Cats</a>: A Facebook group where Singaporean cat owners come together to discuss various cat-related topics, share photos, and ask questions.</li>
                                        </ul>
                                        <p>Join these communities and engage with fellow cat lovers to learn, share, and celebrate the joys of being a cat owner in Singapore!</p>
                                      </div>
                                    `;
                                    break;
                                    case 'resources':
                                        mainContent.innerHTML = `
                                          <h2>Cat-related Resources</h2>
                                          <div class="resources-container">
                                            <h3>Books</h3>
                                            <ul>
                                              <li> "Cat Owner's Guide" by Singapore Cat Lovers Association </li>
                                              <li> "The Complete Guide to Cat Care" by Dr. Emily Tan </li>
                                              <li> "Understanding Your Cat: A Comprehensive Guide" by Sarah Lee </li>
                                            </ul>
                                            <h3>Websites</h3>
                                            <ul>
                                              <li><a href="https://www.catlovers.sg" target="_blank">Cat Lovers Singapore</a> - A comprehensive website providing information about cat care, behavior, and local cat-related events.</li>
                                              <li><a href="https://www.catrescue.sg" target="_blank">Cat Rescue SG</a> - A Singapore-based cat rescue organization dedicated to rescuing and rehoming stray and abandoned cats.</li>
                                              <li><a href="https://www.cathealthsg.com" target="_blank">Cat Health SG</a> - Offers valuable resources on cat health, including information on vaccinations, common illnesses, and preventive care.</li>
                                            </ul>
                                            <h3>Blogs</h3>
                                            <ul>
                                              <li><a href="https://www.meowdiaries.com" target="_blank">Meow Diaries</a> - A blog sharing heartwarming stories, tips, and insights about cats and their well-being.</li>
                                              <li><a href="https://www.purrfectcat.sg" target="_blank">Purrfect Cat SG</a> - Covers a range of topics related to cat care, behavior, and lifestyle, with a focus on Singapore's cat community.</li>
                                              <li><a href="https://www.catsguru.com" target="_blank">Cats Guru</a> - Offers expert advice, tips, and product reviews for cat owners, including specific information relevant to Singapore.</li>
                                            </ul>
                                            <h3>Organizations</h3>
                                            <ul>
                                              <li><a href="https://www.catsociety.sg" target="_blank">Cat Society Singapore</a> - A non-profit organization dedicated to promoting responsible cat ownership and community education about cats.</li>
                                              <li><a href="https://www.catwelfare.org" target="_blank">Cat Welfare Society</a> - Advocates for the welfare of community cats in Singapore, providing rescue, sterilization, and adoption programs.</li>
                                              <li><a href="https://www.sosd.org.sg" target="_blank">Save Our Street Dogs (SOSD)</a> - Although focused on dogs, SOSD often extends support to cat-related issues and collaborates with other organizations.</li>
                                            </ul>
                                          </div>
                                        `;
                                        break;
      default:
        mainContent.innerHTML = '<h2>Page Not Found</h2><p>The requested page does not exist.</p>';
        // Load a 404 page or display an error message
    }
  }
  
let timer;
let score = 0;
let isGameStarted = false;
const gameContainer = document.querySelector('.game-container');

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

  // Reset cat position
  cat.style.top = '0';
  cat.style.left = '0';

  // Set click event on cat image
  cat.addEventListener('click', increaseScore);

  // Move the cat randomly
  moveCat(cat);
  isGameStarted = true;

  // Start the timer
  let timeLeft = 60;
  timer = setInterval(() => {
    timeLeft--;
    timerElement.textContent = 'Time: ' + timeLeft;

    if (timeLeft == 0) {
        endGame();
    }
  }, 1000)
}

function moveCat(cat) {
  const containerSize = gameContainer.offsetWidth;
  const catSize = cat.offsetWidth;
  const maxX = containerSize - catSize;
  const maxY = containerSize - catSize;

  const randomX = Math.floor(Math.random() * maxX);
  const randomY = Math.floor(Math.random() * maxY);

  cat.style.left = randomX + 'px';
  cat.style.top = randomY + 'px';

  // Restrict cat movement within the game area
  cat.style.left = Math.min(Math.max(parseInt(cat.style.left), 0), maxX) + 'px';
  cat.style.top = Math.min(Math.max(parseInt(cat.style.top), 0), maxY) + 'px';

  setTimeout(() => moveCat(cat), 1000);
}

function increaseScore() {
  score++;
  const scoreElement = document.getElementById('score');
  scoreElement.textContent = 'Score: ' + score;
}

function endGame() {
    const cat = document.getElementById('cat');
    const timerElement = document.getElementById('timer');

    // Stop the timer
    clearInterval(timer);

    // Remove click event on cat image
    cat.removeEventListener('click', increaseScore);

    // Set game started flag to false
    isGameStarted = false;

    // Display final score
    timerElement.textContent = 'Time: 0\nFinal Score: ' + score;
}