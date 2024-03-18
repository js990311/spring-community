package com.toyproject.community.controller.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RecentPostCookie {
    public static String COOKIE_NAME = "recentPost";
    public static String COOKIE_DELIM = "_";
    public static int MAX_RECENT_POST_COUNT = 10;
    public static int MAX_COOKIE_AGE = 2*60*60;

    private List<String> recentPosts;

    public RecentPostCookie(HttpServletRequest request){
        Cookie cookie = getCookie(request);
        this.recentPosts = getRecentPost(cookie);
    }

    private Cookie getCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        recentPosts = new ArrayList<>();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(COOKIE_NAME)){
                    return cookie;
                }
            }
        }
        return null;
    }

    private List<String> getRecentPost(Cookie cookie){
        List<String> recentPosts = new ArrayList<>();
        if(cookie == null){
            return recentPosts;
        }
        StringTokenizer st = new StringTokenizer(cookie.getValue(), COOKIE_DELIM);
        while (st.hasMoreTokens()){
            recentPosts.add(st.nextToken());
        }
        return recentPosts;
    }

    public boolean isRecentPost(Long postId){
        return recentPosts.contains(postId.toString());
    }

    public Cookie addPostInRecentPost(Long postId){
        if(!isRecentPost(postId))
            recentPosts.add(postId.toString());

        while (recentPosts.size()> MAX_RECENT_POST_COUNT){
            recentPosts.remove(0);
        }

        Cookie cookie = new Cookie(
                COOKIE_NAME,
                String.join(
                        COOKIE_DELIM,
                        recentPosts
                )
        );

        cookie.setMaxAge(MAX_COOKIE_AGE);
        return cookie;
    }
}
