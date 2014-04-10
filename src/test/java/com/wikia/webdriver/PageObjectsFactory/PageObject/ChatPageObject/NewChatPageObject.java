package com.wikia.webdriver.PageObjectsFactory.PageObject.ChatPageObject;

import com.wikia.webdriver.Common.Core.Assertion;
import com.wikia.webdriver.Common.Core.Global;
import com.wikia.webdriver.Common.Logging.PageObjectLogging;
import com.wikia.webdriver.PageObjectsFactory.PageObject.WikiBasePageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class NewChatPageObject extends WikiBasePageObject
{

	@FindBy(css="textarea[name='message']")
	private WebElement messageWritingArea;
	@FindBy(css="form.Write")
	private WebElement messageForm;
	@FindBy(css="form[class='Write blocked']")
	private WebElement messageWriteAreaBlocked;//when user is disconnected
	@FindBy(xpath="//div[@class='Chat']//li[contains(text(), 'Welcome to the ')]")
	private WebElement welcomeMessage;
	@FindBy(css="div.Rail")
	private WebElement sideBar;
	@FindBy(css="h1[class=public wordmark''] img")
	private WebElement wordmark;
	@FindBy(css="div.User span.username")
	private WebElement userName;
	@FindBy(css="[id*='Chat'] .inline-alert[id*='entry']")
	private WebElement chatInlineAlert;
	@FindBy(css="div.User img")
	private WebElement userAvatar;
	@FindBy(css="#UserStatsMenu .private")
	private WebElement privateMassageButton;
	@FindBy(css="#UserStatsMenu")
	private WebElement userStatsMenu;
	@FindBy(css="li.private-allow")
	private WebElement allowPrivateMassageButton;
	@FindBy(css="li.private-block")
	private WebElement blockPrivateMassageButton;
	@FindBy(css="div#Rail img.wordmark")
	private WebElement mainChatButton;
	@FindBy(css="h1.public.wordmark.selected")
	private WebElement mainChatSelection;
	@FindBy(css="ul.PrivateChatList span.splotch")
	private WebElement privateMessageNotification;
	@FindBy(css="li.User.selected.blocked")
	private WebElement userDisconnectedButton;
	@FindBy(css="li.give-chat-mod span.label")
	private WebElement giveChatModStatusButton;
	@FindBy(css=".continued.inline-alert")
	private WebElement chatInlineAlertContinued;
	@FindBy(css="#UserStatsMenu li.ban")
	private WebElement banUserButton;
	@FindBy(css="#ChatBanModal")
	private WebElement chatBanModal;
	@FindBy(css="#ChatBanModal button.primary")
	private WebElement chatBanModalButton;
	@FindBy (css="#UserStatsMenu .regular-actions li")
	private List<WebElement> userDropDownActionsElements;
	@FindBy (css="#UserStatsMenu .admin-actions li")
	private List<WebElement> adminDropDownActionsElements;
	@FindBy (css="#Rail h1.private")
	private WebElement privateMessagesHeader;
	@FindBy (css="#Rail img.wordmark")
	private WebElement chatWordmarkImage;
	@FindBy (css="#WikiChatList li")
	private WebElement chatLoadedIndicator;

	By userContextMenu = By.cssSelector("ul.regular-actions li");
	By adminContextMenu = By.cssSelector("ul.admin-actions li");
	By privateMessageHeader = By.cssSelector("#Rail h1.private");
	By privateChatHeader = By.cssSelector("#ChatHeader h1.private");

	final private String userUnbanLink = "//a[@data-type='ban-undo' "
			+ "and @data-user='%s']";
	final private String userUnbanConfirmMessage = "//div[@class='Chat']"
			+ "//li[contains(text(), 'has ended the Chat ban for %s')]";

	private final String userSelector = "#user-%s";
	private final String privateMessageUserSelector = "#priv-user-%s";
	private final String privateMessageSelectedUserSelector = "#priv-user-%s.User.selected";
	private final String messageOnChat = "//span[@class='message'][contains(text(), '%s')]";

	public NewChatPageObject(WebDriver driver) {
		super(driver);
	}

	public void verifyChatPage() {
		waitForElementByElement(messageWritingArea);
		waitForElementByElement(chatInlineAlert);
		waitForElementByElement(sideBar);
		waitForElementByElement(userName);
		waitForElementByElement(userAvatar);
		PageObjectLogging.log("verifyChatPage", "Chat page verified", true, driver);
	}

	public void verifyMessageOnChat(String message) {
		waitForElementByXPath(String.format(messageOnChat, message));
		PageObjectLogging.log("VerifyMessageOnChatPresent", "Message: " + message + " is present on chat board", true, driver);
	}

	public void verifyUserJoinToChat(String userName) {
		waitForElementByElement(chatInlineAlertContinued);
		if (!checkIfElementOnPage(userSelector.replace("%user%", userName))) {
			PageObjectLogging.log("VerifyUserJoinsChat", "User: " + userName + " not visible on chat's guests list", false);
			throw new NoSuchElementException("User: " + userName + " not visible on chat's guests list");
		}
		PageObjectLogging.log("verifyUserJoinToChat", userName + " has joined the chat.", true, driver);
	}

	public void verifyUserIsVisibleOnContactsList(String userName)
	{
		waitForElementByXPath("//li[@id='user-"+userName+"']");
		PageObjectLogging.log("verifyUserIsVisibleOnContactsList", userName+" is visible on contacts list", true, driver);
	}

	public void verifyPrivateMessageHeader() {
		waitForElementByElement(privateMessagesHeader);
		PageObjectLogging.log(
			"verifyPrivateMessageHeader",
			"private message header is visible", true, driver
		);
	}

	public void verifyPrivateMessageNotification() {
		waitForElementByElement(privateMessageNotification);
		PageObjectLogging.log(
			"verifyPrivateMessageNotification",
			"private message notification is visible", true, driver
		);
	}
	
	public void verifyPrivateMessageNotification(int notificationNumber)
	{
		waitForElementByXPath("//span[@class='splotch' and contains(text(), '"+notificationNumber+"')]");
		PageObjectLogging.log(
			"verifyPrivateMessageNotification",
			"private message notification number " + notificationNumber + " is visible",
			true
		);
	}

	public void verifyPrivateMessageIsHighLighted(String user) {
		getElementForUser(user, privateMessageSelectedUserSelector);
		PageObjectLogging.log("verifyPrivateMessageIsHighLighted", "private message section is highlighted", true, driver);
	}

	public void verifyPrivateChatTitle(String userName)
	{
		waitForElementByBy(privateChatHeader);
		PageObjectLogging.log("verifyPrivateChatTitle", "private chat title is correct", true, driver);
	}

	public void verifyMainChatIsHighLighted() {
		waitForElementByElement(mainChatSelection);
		PageObjectLogging.log("verifyPrivateMessageIsHighLighted", "private message section is highlighted", true);
	}

	public void verifyNormalUserDropdown(String userName) {
		//This check is needed in case some of the previous tests failed
		//We need to do the clean up in this place - allow provided user to send private messages
		if (checkIfPrivateMessagesNotAllowed(userName)) {
			allowPrivateMessageFromUser(userName);
		}
		Assertion.assertNumber(3, userDropDownActionsElements.size(), "Checking number of elements in the dropDown");
		Assertion.assertEquals("message-wall", userDropDownActionsElements.get(0).getAttribute("class"));
		Assertion.assertEquals("contribs", userDropDownActionsElements.get(1).getAttribute("class"));
		Assertion.assertEquals("private", userDropDownActionsElements.get(2).getAttribute("class"));
	}

	public void verifyBlockingUserDropdown(String userName) {
		clickOnDifferentUser(userName);
		//This check is needed because chat has some lags when it comes to loading dropdown content
		//We need to click it more then once sometimes to actually load everything
		waitForProperNumberOfElementsInUserDropdown(userName);
		List<WebElement> list = userDropDownActionsElements;
		Assertion.assertNumber(3, list.size(), "Checking number of elements in the drop-down");
		Assertion.assertEquals("message-wall", list.get(0).getAttribute("class"));
		Assertion.assertEquals("contribs", list.get(1).getAttribute("class"));
		Assertion.assertEquals("private-allow", list.get(2).getAttribute("class"));	
	}

	public void verifyPrivateUserDropdown(String userName) {
		openUserDropDownInPrivateMessageSection(userName);
		Assertion.assertNumber(3, userDropDownActionsElements.size(), "Checking number of elements in the drop-down");
		Assertion.assertEquals("message-wall", userDropDownActionsElements.get(0).getAttribute("class"));
		Assertion.assertEquals("contribs", userDropDownActionsElements.get(1).getAttribute("class"));
		Assertion.assertEquals("private-block", userDropDownActionsElements.get(2).getAttribute("class"));
	}

	public void verifyAdminUserDropdown(String userName) {
		//Admin dropDown consists of two parts: regular dropdown
		verifyNormalUserDropdown(userName);

		//and admin dropDown
		Assertion.assertNumber(3, adminDropDownActionsElements.size(), "Checking number of elements in the drop-down");
		Assertion.assertEquals("give-chat-mod", adminDropDownActionsElements.get(0).getAttribute("class"));
		Assertion.assertEquals("kick", adminDropDownActionsElements.get(1).getAttribute("class"));
		Assertion.assertEquals("ban", adminDropDownActionsElements.get(2).getAttribute("class"));
	}

	public void verifyUserIsGreyedOut()
	{
		waitForElementByElement(userDisconnectedButton);
		PageObjectLogging.log("verifyUserIsGreyedOut", "Verified user disconnected from the chat", true, driver);
	}
	
	public void verifyWritingAreaIsBlocked()
	{
		waitForElementByElement(messageWriteAreaBlocked);
		PageObjectLogging.log("verifyWritingAreaIsBlocked", "Verified user writing area is blocked", true, driver);
	}
	
	public void verifyUserLeftFromChatMessage(String userName)
	{
		waitForElementByXPath("//li[@class='inline-alert' and contains(text(), '"+userName+" has left the chat.')]");
		PageObjectLogging.log("verifyUserLeftFromChatMessage", "Verified user left message is visible", true, driver);
	}
	
	public void verifyChatModMessage(String userStaff, String userName)
	{
		waitForElementByXPath("//li[contains(text(), '"+userStaff+" has made')]");
		waitForElementByXPath("//li/strong[contains(text(), '"+userName+"')]");
	}
	
	public void disconnectFromChat()
	{
		getUrl(Global.DOMAIN);
		PageObjectLogging.log("disconnectFromChat", "User is disconnected from the chat", true, driver);
	}

	public void writeOnChat(String message) {
		waitForElementByElement(chatLoadedIndicator);
		messageWritingArea.sendKeys(message);
		pressEnter(messageWritingArea);
		PageObjectLogging.log("writeOnChat", "Message: " + message + " written", true, driver);
		verifyMessageOnChat(message);
	}

	public void selectPrivateMessageToUser(String userName) {
		//This check is needed in case some of the previous tests failed
		//We need to do the clean up in this place - allow provided user to send private messages
		if (checkIfPrivateMessagesNotAllowed(userName)) {
			allowPrivateMessageFromUser(userName);
		}
		clickOnDifferentUser(userName);
		waitForElementByElement(privateMassageButton);
		privateMassageButton.click();
		WebElement userInPrivateMessageSection = getElementForUser(userName, privateMessageUserSelector);
		waitForElementVisibleByElement(userInPrivateMessageSection);
		PageObjectLogging.log("selectPrivateMessageToUser", "private message selected from dropdown", true);
	}

	public void selectChatModStatus(WebDriver driver)
	{
		waitForElementByElement(giveChatModStatusButton);
		jQueryClick("li.give-chat-mod span.label");
		PageObjectLogging.log("selectChatModStatus", "chat mod status is clicked", true, driver);
	}

	public void clickOnMainChat() {
		chatWordmarkImage.click();
		PageObjectLogging.log("clickOnMainChat", "main chat is clicked", true);
	}

	public void clickOnPrivateChat(String userName) {
		WebElement privateMessageUser = getElementForUser(userName, privateMessageUserSelector);
		verifyPrivateMessageIsHighLighted(userName);
	}

	public void clickOnUserInPrivateMessageSection(String userName) {
		WebElement privateMessagesUserElement = getElementForUser(userName, privateMessageUserSelector);
		privateMessagesUserElement.click();
		PageObjectLogging.log("clickOnUserInPrivateMessageSection", "private messages user " + userName + " is clicked", true);
	}

	/**
	 * @author Evgeniy (aquilax)
	 * @param userName User name of the user to be banned
	 * @param driver WebDriver in context
	 * clicks on ban user modal
	 */
	private void clickBanUser(String userName, WebDriver driver)
	{
		banUserButton.click();
		waitForElementByElement(chatBanModal);
		PageObjectLogging.log("clickBanUser", "ban user "+userName+" is clicked", true, driver);
	}

	/**
	 * @author Evgeniy (aquilax)
	 * @param userName User name of the user to be banned
	 * @param driver WebDriver in context
	 * Ban user from chat
	 * method should be executed after clickOnDifferentUser()
	 */
	public void banUser(String userName, WebDriver driver)
	{
		clickBanUser(userName, driver);
		chatBanModalButton.click();
		waitForElementNotVisibleByElement(chatBanModal);
		PageObjectLogging.log("clickBanUser", userName + " ban modal is closed",
				true);
	}

	/**
	 * @author Evgeniy (aquilax)
	 * @param userName User name of the user that was unbanned
	 * Check for unban notification message
	 */
	private void verifyChatUnbanMessage(String userName)
	{
		waitForElementByXPath(String.format(userUnbanConfirmMessage, userName));
	}

	/**
	 * @author Evgeniy (aquilax)
	 * @param userName User name of the user to be unbanned
	 * @param driver WebDriver in context
	 * Unban user from chat after the user is banned. Note that the function
	 * relies that the user is banned in the current session.
	 * method should be executed after banUser()
	 */
	public void unBanUser(String userName, WebDriver driver)
	{
		WebElement unbanLink = driver.findElement(By.xpath(
				String.format(userUnbanLink, userName)
		));
		waitForElementByElement(unbanLink);
		unbanLink.click();
		verifyChatUnbanMessage(userName);
		PageObjectLogging.log("unBanUser", userName+" is no longer banned",
			true);
	}

	public void clickOnDifferentUser(String userName) {
		WebElement userOnGuestList = getElementForUser(userName, userSelector);
		boolean hidden = !userStatsMenu.isDisplayed();
		int i = 0;
		//we need this loop because of chat problems - sometimes we need to click more then once
		//to open user dropdown. To avoid infinite loop i (threshold) was introduced
		while (hidden) {
			userOnGuestList.click();
			if (userStatsMenu.isDisplayed() || i >= 10) {
				hidden = false;
			}
			i++;
		}
		PageObjectLogging.log("clickOnDifferentUser", userName + " button clicked", true);
	}

	private void waitForProperNumberOfElementsInUserDropdown(String userName) {
		int i = 0;
		boolean dropdownLoaded = false;
		while (!dropdownLoaded) {
			clickOnDifferentUser(userName);
			if (userDropDownActionsElements.size() == 3 || i >= 10) {
				dropdownLoaded = true;
			} else {
				userAvatar.click();
			}
			i++;
		}
	}

	public void openUserDropDownInPrivateMessageSection(String userName) {
		WebElement userOnPrivateMessagesList = getElementForUser(userName, privateMessageUserSelector);
		boolean hidden = !userStatsMenu.isDisplayed();
		int i = 0;
		while(hidden) {
			userOnPrivateMessagesList.click();
			if (userStatsMenu.isDisplayed() || i >= 10) {
				hidden = false;
			}
			i++;
		}
		PageObjectLogging.log("openUserDropDownInPrivateMessageSection", userName + " button clicked", true);
	}

	public void blockPrivateMessageFromUser(String userName) {
		openUserDropDownInPrivateMessageSection(userName);
		blockPrivateMassageButton.click();
		waitForElementNotVisibleByElement(userStatsMenu);
	}

	public void allowPrivateMessageFromUser(String userName) {
		boolean blocked = true;
		int i = 0;
		//Open user stats dropdown and check if private messages are allowed
		//if are not allowed - allow
		//we need this loop because of chat problems - sometimes we need to click more then once
		//to open user dropdown and allow private messages.
		//To avoid infinite loop i (threshold) was introduced
		while (blocked) {
			clickOnDifferentUser(userName);
			if (checkIfElementOnPage(allowPrivateMassageButton) || i >= 10) {
				allowPrivateMassageButton.click();
				clickOnDifferentUser(userName);
				if (!checkIfElementOnPage(allowPrivateMassageButton)) {
					blocked = false;
				}
			}
			i++;
		}
		PageObjectLogging.log("allowPrivateMessageFromUser", "private messages from " + userName + " are allowed now", true);
	}

	private boolean checkIfPrivateMessagesNotAllowed(String userName) {
		//check is user stats are already open
		// if not open them
		if (!userStatsMenu.isDisplayed()) {
			clickOnDifferentUser(userName);
		}
		return checkIfElementOnPage(allowPrivateMassageButton);
	}

	private WebElement getElementForUser(String userName, String selector) {
		String userCss = String.format(selector, userName);
		waitForElementByCss(userCss);
		return driver.findElement(By.cssSelector(userCss));
	}

}
