// Array with different testimonials for the carousel
const testimonials = [
    {
        text: "Futurus Care saved my trip! My Luna was super well taken care of and I received photos of her every day. I recommend it with my eyes closed!",
        author: "- Maria Silva",
        meta: "Luna's Owner"
    },
    {
        text: "My Mel is elderly and needs daily medication. I was very impressed with the care and attention she received. The communication was perfect and we received photos and videos throughout the day.",
        author: "- Roberto Fernandes",
        meta: "Mel's Owner (Poodle)"
    },
    {
        text: "First time leaving my Simba with pet sitters and it was an incredible experience. He is very fearful of strangers, but created an amazing bond with the caregiver and came home very happy.",
        author: "- Juliana Reis", 
        meta: "Simba's Owner (Golden Retriever)"
    },
    {
        text: "Emergency service saved my Bela's life! In the middle of the night she got sick and the team was super quick to assist us and take her to the vet. Total professionalism in a moment of despair.",
        author: "- Pedro Santos",
        meta: "Bela's Owner (Labrador)"
    },
    {
        text: "I had to travel for work for 10 days and was worried about leaving my two cats. The service was impeccable! I received photos and updates every day, and they came back very well cared for and affectionate.",
        author: "- Carlos Mendez",
        meta: "Pecos and Amendoim's Owner (Mixed Breed Cats)"
    },
    {
        text: "Thor is a large and energetic dog, and I was afraid of how he would react. But FuturusCare professionals demonstrated total knowledge about canine behavior and he was super calm and happy during the walks.",
        author: "- Amanda Costa",
        meta: "Thor's Owner (German Shepherd)"
    }
];

// Global variables for testimonial carousel control
let currentIndex = 0;
const testimonialCard = document.getElementById('testimonialCard');
const testimonialText = document.getElementById('testimonialText');
const testimonialAuthor = document.getElementById('testimonialAuthor');
const testimonialMeta = document.getElementById('testimonialMeta');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');

/**
 * Updates testimonial content with fade animation
 * @param {Object} testimonial - Object containing testimonial data
 */
function updateTestimonial(testimonial) {
    // Start exit animation
    testimonialCard.classList.add('fade-out');
    
    // Wait for exit animation to complete before changing content
    setTimeout(() => {
        testimonialText.textContent = testimonial.text;
        testimonialAuthor.textContent = testimonial.author;
        testimonialMeta.textContent = testimonial.meta;
        
        // Remove exit animation and apply entrance animation
        testimonialCard.classList.remove('fade-out');
        testimonialCard.classList.add('fade-in');
        
        // Remove entrance animation after completion
        setTimeout(() => {
            testimonialCard.classList.remove('fade-in');
        }, 100);
    }, 300);
}

/**
 * Updates navigation button states
 * Disables buttons when necessary
 */
function updateButtons() {
    prevBtn.disabled = currentIndex === 0;
    nextBtn.disabled = currentIndex === testimonials.length - 1;
}

/**
 * Advances to next testimonial
 */
function nextTestimonial() {
    if (currentIndex < testimonials.length - 1) {
        currentIndex++;
        updateTestimonial(testimonials[currentIndex]);
        updateButtons();
    }
}

/**
 * Returns to previous testimonial
 */
function prevTestimonial() {
    if (currentIndex > 0) {
        currentIndex--;
        updateTestimonial(testimonials[currentIndex]);
        updateButtons();
    }
}

/**
 * Progressive counting animation for statistics numbers
 */
function animateNumbers() {
    // Statistics configuration to be animated
    const stats = [
        { element: document.querySelectorAll('.block2-number')[0], target: 500, suffix: '+' },
        { element: document.querySelectorAll('.block2-number')[1], target: 200, suffix: '+' },
        { element: document.querySelectorAll('.block2-number')[2], target: 5, suffix: '' },
        { element: document.querySelectorAll('.block2-number')[3], target: 4.9, suffix: '', decimal: true }
    ];

    // Animate each statistic individually
    stats.forEach(stat => {
        let current = 0;
        const increment = stat.decimal ? stat.target / 100 : stat.target / 50;
        
        // Add animation class
        stat.element.classList.add('counting');
        
        // Timer to gradually increment the number
        const timer = setInterval(() => {
            current += increment;
            
            // Stop animation when reaching target value
            if (current >= stat.target) {
                current = stat.target;
                clearInterval(timer);
                stat.element.classList.remove('counting');
            }
            
            // Update text with appropriate formatting
            if (stat.decimal) {
                stat.element.textContent = current.toFixed(1) + stat.suffix;
            } else {
                stat.element.textContent = Math.floor(current) + stat.suffix;
            }
        }, 40); // 40ms interval for smooth animation
    });
}

/**
 * Initialization when DOM is loaded
 */
document.addEventListener('DOMContentLoaded', function() {
    
    // ==================== MOBILE MENU ====================
    const mobileToggle = document.querySelector('.mobile-menu');
    const navLinks = document.querySelector('.nav-links');
    
    if (mobileToggle && navLinks) {
        // Open/close mobile menu
        mobileToggle.addEventListener('click', function() {
            navLinks.classList.toggle('mobile-active');
            
            // Toggle menu icon
            const icon = mobileToggle.querySelector('i');
            if (navLinks.classList.contains('mobile-active')) {
                icon.className = 'fas fa-times';
            } else {
                icon.className = 'fas fa-bars';
            }
            
            // Prevent scroll when menu is open
            document.body.style.overflow = navLinks.classList.contains('mobile-active') ? 'hidden' : '';
        });
        
        // Close menu when clicking a link
        navLinks.addEventListener('click', function(e) {
            if (e.target.tagName === 'A') {
                navLinks.classList.remove('mobile-active');
                mobileToggle.querySelector('i').className = 'fas fa-bars';
                document.body.style.overflow = '';
            }
        });
        
        // Close menu when clicking outside
        document.addEventListener('click', function(e) {
            if (!navLinks.contains(e.target) && !mobileToggle.contains(e.target)) {
                navLinks.classList.remove('mobile-active');
                mobileToggle.querySelector('i').className = 'fas fa-bars';
                document.body.style.overflow = '';
            }
        });
    }
    
    // ===== OBSERVER SETUP FOR STATISTICS ANIMATION =====
    const statsSection = document.querySelector('.section3');
    let hasAnimated = false; // Flag to avoid multiple animations

    // Intersection Observer to detect when section becomes visible
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            // Start animation when 50% of section is visible
            if (entry.isIntersecting && !hasAnimated) {
                animateNumbers();
                hasAnimated = true;
            }
        });
    }, {
        threshold: 0.5 // Activates when 50% of element is visible
    });

    // Start observing statistics section
    if (statsSection) {
        observer.observe(statsSection);
    }

    // ===== EVENT LISTENERS SETUP =====
    
    // Testimonial navigation buttons
    if (nextBtn && prevBtn) {
        nextBtn.addEventListener('click', nextTestimonial);
        prevBtn.addEventListener('click', prevTestimonial);
        
        // Initialize button states
        updateButtons();
    }

    // ===== EXTRA FEATURES =====
    
    /**
     * Smooth scroll for internal links (if any)
     */
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

    /**
     * Adds hover effect to service cards for touch devices
     */
    const serviceCards = document.querySelectorAll('.block3');
    serviceCards.forEach(card => {
        // For touch devices, add toggle on tap
        card.addEventListener('touchstart', function() {
            // Remove active from other cards
            serviceCards.forEach(otherCard => {
                if (otherCard !== card) {
                    otherCard.classList.remove('touch-active');
                }
            });
            // Toggle on current card
            this.classList.toggle('touch-active');
        });
    });

    /**
     * Lazy loading for images (if implemented in the future)
     */
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });

        // Observe all images with 'lazy' class
        document.querySelectorAll('img[data-src]').forEach(img => {
            imageObserver.observe(img);
        });
    }

    /**
     * Analytics tracking for important button clicks (placeholder)
     */
    document.querySelectorAll('.hero-btn, .button-profs').forEach(button => {
        button.addEventListener('click', function(e) {
            // Here you can add analytics tracking
            console.log('Button clicked:', this.textContent.trim());
            
            // Google Analytics example (uncomment when configured)
            // gtag('event', 'click', {
            //     'event_category': 'CTA',
            //     'event_label': this.textContent.trim()
            // });
        });
    });

    /**
     * Better mobile device experience
     */
    const isMobile = window.innerWidth <= 768;
    if (isMobile) {
        // Disable 3D animations on mobile devices for better performance
        document.body.classList.add('mobile-device');
        
        // Add class for mobile-specific adjustments
        serviceCards.forEach(card => {
            card.classList.add('mobile-card');
        });
    }

    /**
     * Performance optimization - reduce animations if user prefers
     */
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
        // Remove animations for users who prefer reduced motion
        document.body.classList.add('reduced-motion');
    }
});

/**
 * Function for window resizing
 */
window.addEventListener('resize', function() {
    // Adjust layout if necessary
    const isMobile = window.innerWidth <= 768;
    const serviceCards = document.querySelectorAll('.service-card');
    
    if (isMobile) {
        document.body.classList.add('mobile-device');
        serviceCards.forEach(card => {
            card.classList.add('mobile-card');
        });
    } else {
        document.body.classList.remove('mobile-device');
        serviceCards.forEach(card => {
            card.classList.remove('mobile-card', 'touch-active');
        });
    }
});

/**
 * Function to improve accessibility
 */
document.addEventListener('keydown', function(e) {
    // Keyboard navigation in testimonial carousel
    if (e.key === 'ArrowLeft' && !prevBtn.disabled) {
        prevTestimonial();
    } else if (e.key === 'ArrowRight' && !nextBtn.disabled) {
        nextTestimonial();
    }
});

// ===== GLOBAL EXPORTS FOR USE (IF NECESSARY) =====
window.FuturusCare = {
    nextTestimonial,
    prevTestimonial,
    animateNumbers
};
