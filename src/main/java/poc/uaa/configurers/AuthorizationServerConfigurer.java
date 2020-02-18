package poc.uaa.configurers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import lombok.RequiredArgsConstructor;
import poc.uaa.service.CustomDetailsService;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {
	private String clientid = "tutorialspoint";
	private String clientSecret = "$2a$10$FskSEUsyQe473aakDxL3g.ILMaqg5xlwNKbyCHSjmXTkWVV0OWLYe"; // my-secret-key
	private String privateKey = "/priv.key";
	private String publicKey = "/pub.key";

	@Autowired
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	@Autowired
	private final CustomDetailsService customDetailsService;

	@Bean
	public JwtAccessTokenConverter tokenEnhancer() throws IOException {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(IOUtils.resourceToString(privateKey, StandardCharsets.UTF_8));
		converter.setVerifierKey(IOUtils.resourceToString(publicKey, StandardCharsets.UTF_8));
		return converter;
	}

	@Bean
	public JwtTokenStore tokenStore() throws IOException {
		return new JwtTokenStore(tokenEnhancer());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		authenticationManagerBuilder.userDetailsService(customDetailsService);
		endpoints.authenticationManager(authenticationManagerBuilder.getOrBuild())
				.tokenStore(tokenStore())
				.accessTokenConverter(tokenEnhancer());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.allowFormAuthenticationForClients()
				.passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient(clientid)
				.secret(clientSecret)
				.autoApprove(true)
				.authorizedGrantTypes("password", OAuth2AccessToken.REFRESH_TOKEN)
				.accessTokenValiditySeconds(20000)
				.refreshTokenValiditySeconds(20000);

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}