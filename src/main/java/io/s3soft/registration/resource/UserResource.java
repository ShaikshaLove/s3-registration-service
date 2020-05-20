package io.s3soft.registration.resource;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import io.s3soft.registration.dto.SuccessResponse;
import io.s3soft.registration.dto.UserDto;
import io.s3soft.registration.event.OnRegistrationEvent;
import io.s3soft.registration.exception.UserDataException;
import io.s3soft.registration.model.User;
import io.s3soft.registration.model.VerificationToken;
import io.s3soft.registration.service.IUserService;
import io.s3soft.registration.validator.UserDtoValidator;
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*",methods = {RequestMethod.DELETE,
        RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class UserResource {
	@Autowired
	private UserDtoValidator userDtoValidator;
	@Autowired
	private IUserService userService;

	@Autowired 
	private ApplicationEventPublisher eventPublisher;
    
	
	@PostMapping(consumes= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> registerUserAccount(@RequestBody UserDto accountDto,
			/* WebRequest request, */BindingResult result)throws UserDataException {
		userDtoValidator.validate(accountDto, result); 
		if(result.hasErrors()) {
			throw new UserDataException("Validations errors in user data ", result);
		}else {
			User registeredUser=userService.registerNewUserAccount(accountDto);	
			//String appUrl=request.getContextPath();
			//eventPublisher.publishEvent(new OnRegistrationEvent(registeredUser,appUrl));
			return new ResponseEntity<SuccessResponse<String>>(new SuccessResponse<String>("The user registration has been saved:"+registeredUser.getUserId(),HttpStatus.OK.value()),HttpStatus.CREATED);
		}
	}

	@GetMapping(value="/{userName}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<User>> getUser(@PathVariable("userName")String userName) {
		return ResponseEntity.ok(new SuccessResponse<User>(userService.findByEmail(userName),HttpStatus.OK.value()));
	}

	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllUser() {
		return ResponseEntity.ok(new SuccessResponse<List<User>>(userService.findAll(),HttpStatus.OK.value()));
	}

	@PutMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> UpdateUser(@RequestBody User user) {
		user=userService.updateUser(user);
		return ResponseEntity.ok(new SuccessResponse<User>(user, HttpStatus.OK.value()));
	}

	@DeleteMapping(value="/{userId}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteUser(@PathVariable("userId") int id){
		userService.deleteUserById(id);
		return ResponseEntity.ok(new SuccessResponse<String>("The User details has been removed ",HttpStatus.OK.value()));
	}

	@GetMapping(value="/conformRegistration",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> conformRegistration(@RequestParam("token")String verificationToken,@RequestParam("firstName")String firstName){
		VerificationToken vt=userService.getVerificationToken(verificationToken);
		User user=vt.getUser();
		Calendar cl=Calendar.getInstance();
		if((vt.getExpiryDate().getTime()-cl.getTime().getTime())<=0) {
			return  ResponseEntity.ok(new SuccessResponse<String>("The link is expired... Click on resend link",HttpStatus.OK.value()));
		}else {
		user.setEnabled(true);
		userService.saveRegisteredUser(user);
		return ResponseEntity.ok(new SuccessResponse<String>("Hurray!!! Your account has been activated",HttpStatus.OK.value()));
		}
	}

}
