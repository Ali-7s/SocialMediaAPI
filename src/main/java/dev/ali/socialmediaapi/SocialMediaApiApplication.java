package dev.ali.socialmediaapi;

import dev.ali.socialmediaapi.model.Post;
import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.repository.PostRepository;
import dev.ali.socialmediaapi.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SocialMediaApiApplication implements CommandLineRunner {

    private final UserService userService;
    private final PostRepository postRepository;

    @Autowired
    public SocialMediaApiApplication(UserService userService, PostRepository postRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SocialMediaApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Create users and add them to the service
        User user1 = new User("MadarameBK", "Baku Madarame", "jamyyyy@gmail.com", "password1");
        userService.addUser(user1);
        Post post1 = new Post("He is Usogui, The Lie Eater!!!", user1);
        post1.setCreatedAt(post1.getCreatedAt().minusMinutes(8));
        postRepository.save(post1);

        User user2 = new User("clare_aflo", "Clare Doe", "jane.doe@example.com", "password2");
        userService.addUser(user2);
        Post post2 = new Post("Exploring the beauty of everyday life", user2);
        post2.setCreatedAt(post1.getCreatedAt().minusMinutes(5));
        postRepository.save(post2);

        User user3 = new User("john_stinger", "Johnny Smithens", "john.smith@example.com", "password3");
        userService.addUser(user3);
        Post post3 = new Post("The latest in technology and innovation", user3);
        post3.setCreatedAt(post1.getCreatedAt().minusMinutes(44));
        postRepository.save(post3);

        User user4 = new User("saraConno_r", "Sara Connor", "sara.connor@example.com", "password4");
        userService.addUser(user4);
        Post post4 = new Post("My adventures around the world", user4);
        post4.setCreatedAt(post1.getCreatedAt().minusDays(10).minusMinutes(8));
        postRepository.save(post4);

        User user5 = new User("mikeWazowski", "Wazzy Mike", "mike.w@example.com", "password5");
        userService.addUser(user5);
        Post post5 = new Post("Analyzing the latest blockbusters", user5);
        post5.setCreatedAt(post1.getCreatedAt().minusMinutes(23));
        postRepository.save(post5);

        User user6 = new User("alexParker", "Spidey Parker", "alex.parker@example.com", "password6");
        userService.addUser(user6);
        Post post6 = new Post("Tips for a healthier lifestyle", user6);
        post6.setCreatedAt(post1.getCreatedAt().minusHours(7).minusMinutes(8));
        postRepository.save(post6);

        User user7 = new User("emmaJones", "Emmy J", "emma.jones@example.com", "password7");
        userService.addUser(user7);
        Post post7 = new Post("The future of fashion trends", user7);
        postRepository.save(post7);

        User user8 = new User("lucasBrown", "Luca ¯³", "lucas.brown@example.com", "password8");
        userService.addUser(user8);
        Post post8 = new Post("Journey through the cosmos", user8);
        postRepository.save(post8);

        User user9 = new User("oliviaGreen", "Olivia", "olivia.green@example.com", "password9");
        userService.addUser(user9);
        Post post9 = new Post("Exploring the world of culinary delights", user9);
        post9.setCreatedAt(post1.getCreatedAt().minusDays(2).minusMinutes(34));
        postRepository.save(post9);

        User user10 = new User("ethanHunt", "Hunt", "ethan.hunt@example.com", "password10");
        userService.addUser(user10);
        Post post10 = new Post("Tales of a secret agent", user10);
        post10.setCreatedAt(post10.getCreatedAt().minusHours(3).minusMinutes(24));
        postRepository.save(post10);

        User user11 = new User("graceAdams", "Grace 👀", "grace.adams@example.com", "password11");
        userService.addUser(user11);
        Post post11 = new Post("Exploring the depths of art", user11);
        post11.setCreatedAt(post11.getCreatedAt().minusHours(10).minusMinutes(34));
        postRepository.save(post11);

        // For users with multiple posts
        User user12 = new User("aliceWonders", "Alice in Wonderland", "alice.wonders@example.com", "password12");
        userService.addUser(user12);
        Post post12 = new Post("Discover the joy of gardening", user12);
        Post post13 = new Post("Natural healing with herbs", user12);
        post12.setCreatedAt(post12.getCreatedAt().minusDays(4).minusMinutes(20));
        post13.setCreatedAt(post13.getCreatedAt().minusHours(1).minusMinutes(1));
        postRepository.save(post12);
        postRepository.save(post13);

        User user13 = new User("bobBuilder", "Builda B", "bob.builder@example.com", "password13");
        userService.addUser(user13);
        Post post14 = new Post("Build your dreams", user13);
        Post post15 = new Post("Crafting with wood", user13);
        post14.setCreatedAt(post1.getCreatedAt().minusMinutes(34));
        post15.setCreatedAt(post1.getCreatedAt().minusMinutes(13));
        postRepository.save(post14);
        postRepository.save(post15);

        User user14 = new User("claraCloud", "Cloud 9 Clara", "clara.cloud@example.com", "password14");
        userService.addUser(user14);
        Post post16 = new Post("Understanding the cloud", user14);
        Post post17 = new Post("The future of artificial intelligence", user14);
        Post post18 = new Post("Exploring big data concepts", user14);
        post16.setCreatedAt(post1.getCreatedAt().minusDays(1).minusMinutes(4));
        post17.setCreatedAt(post1.getCreatedAt().minusDays(3).minusMinutes(34));
        post18.setCreatedAt(post1.getCreatedAt().minusDays(5).minusMinutes(34));
        postRepository.save(post16);
        postRepository.save(post17);
        postRepository.save(post18);

        User user15 = new User("daveDrummer", "Drummer Dave", "dave.drummer@example.com", "password15");
        userService.addUser(user15);
        Post post19 = new Post("Mastering the drums", user15);
        Post post20 = new Post("Exploring global music", user15);
        Post post21 = new Post("A world of percussion", user15);
        postRepository.save(post19);
        postRepository.save(post20);
        postRepository.save(post21);
    }
}
