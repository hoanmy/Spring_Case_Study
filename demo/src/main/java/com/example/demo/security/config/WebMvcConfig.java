package com.example.demo.security.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.example.demo.controller.AccessConfirmationController;
import com.example.demo.controller.AdminController;
import com.example.demo.controller.PhotoController;
import com.example.demo.controller.PhotoServiceUserController;
import com.example.demo.model.PhotoInfo;
import com.example.demo.services.IPhotoService;
import com.example.demo.services.PhotoServiceImpl;

//@Configuration
//@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
		ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
		contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setViewClass(JstlView.class);

		MappingJackson2JsonView defaultView = new MappingJackson2JsonView();
		defaultView.setExtractValueFromSingleKeyModel(true);

		ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
		contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
		contentViewResolver.setViewResolvers(Arrays.<ViewResolver> asList(viewResolver));
		contentViewResolver.setDefaultViews(Arrays.<View> asList(defaultView));
		return contentViewResolver;
	}

//	@Qualifier("PhotoServiceUserController")
//	@Bean
//	public PhotoServiceUserController photoServiceUserController(IPhotoService photoService) {
//		PhotoServiceUserController photoServiceUserController = new PhotoServiceUserController();
//		return photoServiceUserController;
//	}

//	@Qualifier("PhotoController")
//	@Bean
//	public PhotoController photoController(IPhotoService photoService) {
//		PhotoController photoController = new PhotoController();
//		photoController.setPhotoService(photoService);
//		return photoController;
//	}

//	@Qualifier("AccessConfirmationController")
//	@Bean
//	public AccessConfirmationController accessConfirmationController(ClientDetailsService clientDetailsService,
//			ApprovalStore approvalStore) {
//		AccessConfirmationController accessConfirmationController = new AccessConfirmationController();
//		accessConfirmationController.setClientDetailsService(clientDetailsService);
//		accessConfirmationController.setApprovalStore(approvalStore);
//		return accessConfirmationController;
//	}

	@Bean
	public PhotoServiceImpl photoServices() {
		List<PhotoInfo> photos = new ArrayList<PhotoInfo>();
		photos.add(createPhoto("1", "marissa"));
		photos.add(createPhoto("2", "paul"));
		photos.add(createPhoto("3", "marissa"));
		photos.add(createPhoto("4", "paul"));
		photos.add(createPhoto("5", "marissa"));
		photos.add(createPhoto("6", "paul"));

		PhotoServiceImpl photoServices = new PhotoServiceImpl();
		photoServices.setPhotos(photos);
		return photoServices;
	}

//	@Qualifier("AdminController")
//	@Bean
//	public AdminController adminController(TokenStore tokenStore,
//			@Qualifier("consumerTokenServices") ConsumerTokenServices tokenServices,
//			UserApprovalHandlerImpl userApprovalHandler) {
//		AdminController adminController = new AdminController();
//		adminController.setTokenStore(tokenStore);
//		adminController.setTokenServices(tokenServices);
//		adminController.setUserApprovalHandler(userApprovalHandler);
//		return adminController;
//	}

	private PhotoInfo createPhoto(String id, String userId) {
		PhotoInfo photo = new PhotoInfo();
		photo.setId(id);
		photo.setName("photo" + id + ".jpg");
		photo.setUserId(userId);
		photo.setResourceURL("/sparklr/" + photo.getName());
		return photo;
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
}
