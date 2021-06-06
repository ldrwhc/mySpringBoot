package com.njupt.whc.controller;

import com.njupt.whc.mapper.QuestionMapper;
import com.njupt.whc.mapper.UserMapper;
import com.njupt.whc.model.Question;
import com.njupt.whc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "questiontitle") String questiontitle,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "tag") String tag,
            HttpServletRequest request,
            Model model){

        model.addAttribute("questiontitle",questiontitle);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (questiontitle == null || questiontitle==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null || description==""){
            model.addAttribute("error","描述不能为空");
            return "publish";
        }
        if (tag == null || tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        User user = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0)
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    user= userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }

        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setQuestiontitle(questiontitle);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question); //ctrl+alt+v注释出来
        return "redirect:/";
    }
}
