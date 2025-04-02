package com.example.samuraitravel.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.samuraitravel.entity.User;

public class UserDetailsImpl implements UserDetails {
	
	private final User user;
	private final Collection<GrantedAuthority> authorities;
	
	//コンストラクタ
	public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}
	
	//ユーザー情報取得
	public User getUser() {
		return user;
	}
	
	//パスワード取得(ハッシュ化済み)
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	//ユーザー名(ログインIDとしてメールアドレスを使用)
	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	//権限情報の取得
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return authorities;
	}
	
	//↓全て true であればログイン成功
	
	//アカウント有効期限
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	//アカウントロック状態
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	//資格情報の有効期限
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	//アカウントの有効状態
	@Override
	public boolean isEnabled() {
		return user.getEnabled();
	}
}

