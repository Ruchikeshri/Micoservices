package com.example.myproj.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.example.myproj.Exception.BlogAlreadyExistsException;
import com.example.myproj.Exception.BlogNotFoundException;
import com.example.myproj.Repository.PostRepository;
import com.example.myproj.model.Blog;
import com.example.myproj.model.Type;
import com.example.myproj.userservice.model.RegisterAndLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myproj.Repository.BlogRepository;
import org.springframework.web.client.RestTemplate;

@Service
public class BlogServiceImpl implements BlogService {

    private BlogRepository blogRepository;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Autowired
    private RestTemplate template;
    @Autowired
    private PostRepository postRepository;

    @Override
    public Blog SaveBlog(Blog blog) throws BlogAlreadyExistsException {
        // TODO Auto-generated method stub
//		if(blogRepository.existsById(blog.getBlogId()))
//		{
//			throw new BlogAlreadyExistsException();
//
//		}
        Optional<Blog> optional = Optional.ofNullable(blogRepository.findByName(blog.getAuthorName()));
        if (optional.isPresent()) {
            throw new BlogAlreadyExistsException("BlogAlreadyExists");
        } else {
            Blog SavedBlog = blogRepository.save(blog);
            return SavedBlog;
        }
    }

    @Override
    public Blog addComment(Type type) throws BlogAlreadyExistsException, BlogNotFoundException {
        Optional<Blog> b1 = blogRepository.findById(type.getBlogId());
        boolean isPresent = false;
        List<Type> type1 = new LinkedList<>();
//      if(b1.get().getBlogId()==type.getBlogId())
        if (b1.get().getTypes().get(0).getName().equals("string")) {
//          type1 = b1.get().getTypes();
            type1.add(type);

        } else {
            type1 = b1.get().getTypes();
            type1.add(type);
        }
        b1.get().setTypes(type1);
        return blogRepository.save(b1.get());
    }

    @Override
    public RegisterAndLogin PostComment(String email, RegisterAndLogin us) throws BlogNotFoundException {
        String url = "http://USER-SERVICE/api/v1/user?email=" + email;
        String user = template.getForObject(url, String.class);

        Optional<Blog> B1 = blogRepository.findById(us.getBlogId());
//        Type type= (Type) B1.get().getTypes();
        List<Type> type = us.getType().stream().collect(Collectors.toList());
        RegisterAndLogin user2 = new RegisterAndLogin();
        List<Type> type2 = new LinkedList<>();
        if (user.length() != 0) {
//            Optional<Blog> B1=blogRepository.findById(blog.getBlogId());
//            type2 = us.getType();
            if (B1.isPresent()) {


                for (int i = 0; i < type.size(); i++) {
                    type2.add(type.get(i));
                }

//                 B1.get().setTypes(type2);
//                type2.add(B1.get().getTypes().get())

//                for(int i =0;i<B1.get().getTypes().size();i++)
//                    B1.get().getTypes().get(i).setComment(B1.get().getTypes().get(i).getComment());

            }
//            user2.setType(user1);
            user2.setType(type2);
            user2.setBlogId(B1.get().getBlogId());
            user2.setPostId((UUID.randomUUID().toString()));
            user2.setEmail(user);


        }
        return postRepository.save(user2);
    }

    @Override
    public RegisterAndLogin addLoggedComment(Type type, String email, String PostId) throws BlogAlreadyExistsException, BlogNotFoundException {
//        Optional<Blog> b1 = blogRepository.findById(type.getBlogId());

        String url = "http://USER-SERVICE/api/v1/user?email=" + email;
        String use = template.getForObject(url, String.class);

//        Optional<Blog> B1 = blogRepository.findById(type.getBlogId());

//        Optional<RegisterAndLogin> user = postRepository.findById(type.getBlogId());
//        postRepository.findById(type.getBlogId());
        Optional<RegisterAndLogin> user = postRepository.findById(type.getBlogId());
        RegisterAndLogin user2 = new RegisterAndLogin();
//        List<Type> type4 = type.getType().stream().collect(Collectors.toList());
//       List<RegisterAndLogin> reg = user.stream().collect(Collectors.toList());
        List<RegisterAndLogin> reg = postRepository.findAll();
        System.out.println(reg);
        System.out.println("******************************************88" + "user-" + user.get().getBlogId());
        boolean isPresent = false;
        List<Type> type1 = new LinkedList<>();
//      if(b1.get().getBlogId()==type.getBlogId())
//        if (user.get().getType().get(0).getName().equals("string")) {
//          type1 = user.get().getType();
//            type1.add(type);
//
//        } else {
//            type1 = user.get().getType();
        int index = 0;
//        if(!type.getPostId().equals(null)) {
//            for(int j=0;j<type4.size();j++)
//            for (int i=0;i<type.getType().size();i++)
//            {  if(type.getType().size()>index)
//                index = type4.size();
//                type1.add(type4.get(i));
//                index++;
//            }
//            type1.add(type.getType().get(i));
//        for (int i=0;i<reg.size();i++) {
//            for (int j = 0; j < reg.get(i).getType().size(); j++)
//                if (reg.get(i).getType().isEmpty())
//        if (!user.isPresent()) {
//            type1.add(type);
//        } else {
//                    type1 = user.get().getType();
//                    type1 = reg.get()
//                    j++;
//                    type1.add(type.)
//                    type1.add(type);
//                    type1 = reg.get(i).getType();
//                    type1.add(type);
//                    user2.setType(reg.get(i).getType());

            type1 = user.get().getType();
            type1.add(type);
//        }

//        }
//            type.getType().add();

//            type.setType(type1);

//        }
//        }
//        user2.setBlogId(type.getBlogId());
        user2.setType(type1);
        user2.setEmail(use);
        user2.setPostId(PostId);
        user2.setBlogId(user.get().getBlogId());
//       user.get().setType(type1);
//       user2.setPostId(user.get().getPostId());


//        user.get().setBlogId(B1.get().getBlogId());
//        user.get().setType(type1);

//      user2.getType().stream().collect(Collectors.toList());
//      List<RegisterAndLogin> users = (List<RegisterAndLogin>) user2.getType().stream().collect(Collectors.toList());
        return postRepository.save(user2);
    }


    @Override
    public Blog deleteComment(int BlogId, int val) throws BlogNotFoundException {
        Optional<Blog> blog = Optional.ofNullable(blogRepository.findById(BlogId).orElse(null));
        List<Type> type = (List<Type>) blog.get().getTypes();
        int index = 0;
        boolean found = false;
        for (int i = 0; i < type.size(); i++) {
            if (type.get(i).getBlogId() == blog.get().getBlogId()) {
                index = i;
                found = true;
                break;
            }
        }
        if (found) {
            Type types1 = type.get(val);
            type.remove(val);
            blog.get().setTypes(type);

            return blogRepository.save(blog.get());
        } else {
            throw new BlogNotFoundException();
        }

    }

    @Override
    public Blog updateComment(List<Type> type) throws BlogNotFoundException {
        Blog updates = null;

        for (int i = 0; i < type.size(); i++) {
            Optional<Blog> blog = blogRepository.findById(type.get(i).getBlogId());
            List<Type> type1 = null;

            if (blog.isPresent()) {

//       Optional<Blog> blog1 = blogRepository.findById(type.getBlogId());
                Blog blog1 = getBlogById(blog.get().getBlogId());
                blog1.getTypes().get(i).setComment(type.get(i).getComment());
//                blog1.get().getTypes().get(i).setComment(type.get(i).getComment());

                updates = blogRepository.save(blog1);
//         Blog ActualBlog = getBlogById(blog.getBlogId());
//         ActualBlog.setAuthorName(blog.getAuthorName());
//         UpdatedBlog = blogRepository.save(ActualBlog);
            }

        }
        return updates;
    }

    @Override
    public Optional<RegisterAndLogin> getBlogId(int BlogId ) throws BlogNotFoundException {
        return Optional.of(postRepository.findById(String.valueOf(BlogId)).get());
    }


    @Override
    public List<Blog> getAllBlogs() {
        // TODO Auto-generated method stub
        List<Blog> b1 = blogRepository.findAll();
//		Collections.sort(b1,new Comparator<Blog>(){
//			@Override
//			public int compare(Blog o1, Blog o2) {
//				return o1.getAuthorName().compareTo(o2.getAuthorName());}
//		});
//		Collections.sort(b1,new MyComparator());

//        Collections.sort(b1, (o1, o2) ->
//                o1.getAuthorName().compareTo(o2.getAuthorName()));
        Collections.sort(b1,Comparator.comparing(Blog::getAuthorName));

        return b1;

//return b1;
//		return(List<Blog>) blogRepository.findAll();

    }

    //	private class MyComparator implements Comparator<Blog> {
//		@Override
//		public int compare(Blog o1, Blog o2) {
//			return o1.getAuthorName().compareTo(o2.getAuthorName());
//		}
//	}

    @Override
    public Blog getBlogById(int id) throws BlogNotFoundException {
        // TODO Auto-generated method stub
        Blog blog = null;

        Optional optional = blogRepository.findById(id);
        if (optional.isPresent()) {
            blog = blogRepository.findById(id).get();
        }
//		}
        else {
            throw new BlogNotFoundException();
        }
        return blog;


    }


    @Override
    public Blog deleteById(int id) throws BlogNotFoundException {
        // TODO Auto-generated method stub
        Blog blog = null;
        Optional optional = blogRepository.findById(id);
        if (optional.isPresent()) {
            blog = blogRepository.findById(id).get();
            blogRepository.deleteById(id);
        } else {
            throw new BlogNotFoundException();
        }
        return blog;
    }

    @Override
    public Blog updateBlog(Blog blog) throws BlogAlreadyExistsException, BlogNotFoundException {
        // TODO Auto-generated method stub

        Blog UpdatedBlog = null;
        boolean bool = blogRepository.existsById(blog.getBlogId());
        if (bool) {
            Blog ActualBlog = getBlogById(blog.getBlogId());
            ActualBlog.setAuthorName(blog.getAuthorName());
            UpdatedBlog = blogRepository.save(ActualBlog);
        } else {
            throw new BlogNotFoundException();
        }
        return UpdatedBlog;
    }


}
