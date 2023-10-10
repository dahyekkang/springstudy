package com.gdu.app09.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.app09.dto.ContactDto;
import com.gdu.app09.service.ContactService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j  // private static final Logger log = LoggerFactory.getLogger(ContactController.class);
@RequiredArgsConstructor    // private final ContactService contactService;에 @Autowired를 하기 위한 코드이다.
@Controller
public class ContactController {
  
  // ContactController를 실행할 때 org.slf4j.Logger가 동작한다.
  // private static final Logger log = LoggerFactory.getLogger(ContactController.class);    // ContactController가 실행될 때 발생하는 로거의 로그를 찍는다.
  
  private final ContactService contactService;
  
  @RequestMapping(value="/contact/list.do", method=RequestMethod.GET)   // location.href는 get방식이다.
  public String list(Model model) {
    List<ContactDto> contactList = contactService.getContactList();
    model.addAttribute("contactList", contactList);
    return "contact/list";
  }
  
  @RequestMapping(value="/contact/write.do", method=RequestMethod.GET)
  public String write() {       // 단순히 jsp로 넘어가는 것
    return "contact/write";
  }
  
  @RequestMapping(value="/contact/add.do", method=RequestMethod.POST)
  public String add(ContactDto contactDto, RedirectAttributes redirectAttributes) {    // insert했으면 redirect한다 !
    log.info("add:" + contactDto);
    int addResult = contactService.addContact(contactDto);    // addResult는 어떻게 전달할 것이냐?! -> RedirectAttributes
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/contact/list.do";   // 목록보기로 redirect, addResult가 list.jsp로 전달된다.
  }
  
  @RequestMapping(value="/contact/detail.do", method=RequestMethod.GET)
  public String detail(@RequestParam(value="contact_no", required=false, defaultValue="0") int contact_no, Model model) {
    log.info("detail:" + contact_no);
    // SELECT의 결과는 forward 대상이다.(model사용)
    model.addAttribute("contact", contactService.getContactByNo(contact_no));
    return "contact/detail";
  }
  
  @RequestMapping(value="/contact/modify.do", method=RequestMethod.POST)
  public String modify(ContactDto contactDto, RedirectAttributes redirectAttributes) {
    log.info("modify:" + contactDto);
    int modifyResult = contactService.modifyContact(contactDto);
    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
    return "redirect:/contact/detail.do?contact_no=" + contactDto.getContact_no();
  }
  
  @RequestMapping(value="/contact/delete.do", method=RequestMethod.POST)    // delete할 때, POST 권장
  public String delete(@RequestParam(value="contact_no", required=false, defaultValue="0") int contact_no, RedirectAttributes redirectAttributes) {
    log.info("delete:" + contact_no);
    int deleteResult = contactService.deleteContact(contact_no);
    redirectAttributes.addFlashAttribute("deleteResult", deleteResult);
    return "redirect:/contact/list.do";
  }

}
