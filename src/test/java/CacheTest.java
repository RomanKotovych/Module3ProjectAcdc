import com.javarush.kotovych.entity.User;
import com.javarush.kotovych.factory.SessionCreator;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class CacheTest {

    @Test
    public void read(){
        for(int i = 0; i < 1000; i++){
            try(Session session = SessionCreator.createSession()){
                User user = session.get(User.class, 1L);
                System.out.println(user);
            }
        }
    }
}