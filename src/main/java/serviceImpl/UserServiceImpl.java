package serviceImpl;

import annotation.Auth;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import repository.UserMapper;
import service.UserService;
import util.JwtUtil;

import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean signUp(User user){
        if(user.getAccount().equals(userMapper.getUserByAccount(user.getAccount()))){
            return false;
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10)));
        return userMapper.createUser(user);
    }

    @Override
    public HashMap<String, String> login(User user) throws Exception{
        User select = userMapper.getUserByAccount(user.getAccount());

        if(select == null) throw new Exception("유저가 존재하지 않습니다.");
        if(!BCrypt.checkpw(user.getPassword(), select.getPassword())) throw new Exception("비밀번호 오류");

        HashMap<String, String> ret = new HashMap<>();
        ret.put("token", jwtUtil.genJsonWebToken(select.getId()));
        return ret;
    }

    @Override
    public boolean deleteAccount() throws Exception{
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");
        Long id = jwtUtil.getIdFromJWT(token);
        if(userMapper.getUserByID(id) == null) throw new Exception("유저가 존재하지 않습니다.");

        return userMapper.deletedUser(id);
    }

    @Override
    public boolean updateAccount(User user) throws Exception{
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");
        Long id = jwtUtil.getIdFromJWT(token);
        if(userMapper.getUserByID(id) == null) throw new Exception("유저가 존재하지 않습니다.");

        user.setId(id);
        return userMapper.updateUser(user);
    }

    @Override
    public User getUser() throws Exception{
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");
        Long id = jwtUtil.getIdFromJWT(token);
        User me = userMapper.getUserByID(id);
        if(me == null) throw new Exception("유저가 존재하지 않습니다.");
        me.setPassword(null);
        return me;
    }
}
