package org.spica.javaclient.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spica.javaclient.exceptions.NotFoundException;

public class ModelTest {

  private LocalDate today = LocalDate.now();

  @Test public void findUserMixedCase() {
    Model model = new Model();

    UserInfo existingUser = new UserInfo().id("1").username("existinguser").email("Existinguser@Spica.org");
    model.getUserInfos().add(existingUser);
    UserInfo foundUser = model.findUserByMail("existinguser@spica.org");
    Assert.assertEquals(existingUser, foundUser);

  }

  @Test public void getUserNotNull() throws NotFoundException {
    Model model = new Model();

    UserInfo existingUser = new UserInfo().id("1").username("existinguser").email("existinguser@spica.org");
    model.getUserInfos().add(existingUser);

    Assertions.assertThrows(NotFoundException.class, () -> {
      model.getUserNotNull("@user");
    });

    UserInfo userInfo2 = model.getUserNotNull("other.user@spica.org");
    Assert.assertEquals(2, model.getUserInfos().size());
    Assert.assertEquals(userInfo2, model.getUserInfos().get(1));
    Assert.assertEquals("other.user@spica.org", userInfo2.getEmail());

    UserInfo userInfo3 = model.getUserNotNull("@existinguser");
    Assert.assertEquals(2, model.getUserInfos().size());
    Assert.assertEquals(existingUser, userInfo3);

    UserInfo userInfo4 = model.getUserNotNull("existinguser@spica.org");
    Assert.assertEquals(2, model.getUserInfos().size());
    Assert.assertEquals(existingUser, userInfo4);

    UserInfo userInfo5 = model.getUserNotNull("1");
    Assert.assertEquals(2, model.getUserInfos().size());
    Assert.assertEquals(existingUser, userInfo5);

  }

  @Test public void getUserNotNullInvalidId() throws NotFoundException {
    Assertions.assertThrows(NotFoundException.class, () -> {
      Model model = new Model();

      UserInfo existingUser = new UserInfo().id("1").username("existinguser").email("existinguser@spica.org");
      model.getUserInfos().add(existingUser);
      model.getUserNotNull("2");
    });
  }

  @Test public void getOtherUsers() throws NotFoundException {

    UserInfo me = new UserInfo().username("me").id("1").email("me@spica.org");
    UserInfo other = new UserInfo().username("other").id("2").email("me@spica.org");
    UserInfo other2 = new UserInfo().username("other2").id("3").email("me@spica.org");

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo();
    messagecontainerInfo.addMessageItem(
        new MessageInfo().addRecieversToItem("externalUser@spica.org").creator("@me")); //me->externalUser
    messagecontainerInfo.addMessageItem(new MessageInfo().addRecieversToItem("@other2").creator("@me")); //me-> other2
    messagecontainerInfo.addMessageItem(new MessageInfo().addRecieversToItem("2")); //me->other
    messagecontainerInfo.addMessageItem(new MessageInfo().addRecieversToItem("@me").creator("2"));

    Model model = new Model();
    model.getUserInfos().addAll(Arrays.asList(me, other, other2));
    model.setMe(me);
    List<UserInfo> otherUsers = model.getOtherUsers(messagecontainerInfo);
    Assert.assertEquals(3, otherUsers.size());
    Assert.assertTrue(otherUsers.contains(other));
    Assert.assertTrue(otherUsers.contains(other2));
    Assert.assertFalse(otherUsers.contains(me));

  }

  @Test public void findUserBySearchString() throws NotFoundException {

    UserInfo user = new UserInfo().id("id").email("user@spica.org").username("user");
    UserInfo invaliduser = new UserInfo().id("id2").email("user2@spica.org").username("user2");
    Model model = new Model();
    model.getUserInfos().addAll(Arrays.asList(user, invaliduser));

    Assert.assertEquals(user, model.findUserBySearchString("id")); //per ID
    Assert.assertEquals(user, model.findUserBySearchString("user@spica.org")); //per mail
    Assert.assertEquals(user, model.findUserBySearchString("@user")); //per ID
  }

  @Test public void findUserBySearchStringNull() {

    Assertions.assertThrows(NotFoundException.class, () -> {

      UserInfo user = new UserInfo().id("id").email("user@spica.org").username("user");
      UserInfo invaliduser = new UserInfo().id("id2").email("user2@spica.org").username("user2");
      Model model = new Model();
      model.getUserInfos().addAll(Arrays.asList(user, invaliduser));

      model.findUserBySearchString(null);
    });
  }

  @Test public void findUserBySearchStringEmpty() {
    Assertions.assertThrows(NotFoundException.class, () -> {

      UserInfo user = new UserInfo().id("id").email("user@spica.org").username("user");
      UserInfo invaliduser = new UserInfo().id("id2").email("user2@spica.org").username("user2");
      Model model = new Model();
      model.getUserInfos().addAll(Arrays.asList(user, invaliduser));

      model.findUserBySearchString("    ");
    });
  }

  @Test public void findUserBySearchStringNotFound() throws NotFoundException {
    Assertions.assertThrows(NotFoundException.class, () -> {

      UserInfo user = new UserInfo().id("id").email("user@spica.org").username("user");
      UserInfo invaliduser = new UserInfo().id("id2").email("user2@spica.org").username("user2");
      Model model = new Model();
      model.getUserInfos().addAll(Arrays.asList(user, invaliduser));

      model.findUserBySearchString("id3");
    });
  }

  @Test public void findEventBefore() {
    EventInfo event1 = new EventInfo().eventType(EventType.TASK)
        .start(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 8, 0));
    Model model = new Model();
    model.getEventInfosReal().add(event1);

    Assert.assertNull(
        model.findEventBefore(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 7, 0)));
    Assert.assertNull(
        model.findEventBefore(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 8, 0)));
    Assert.assertNotNull(
        model.findEventBefore(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 9, 0)));
  }

  @Test public void findEventAfter() {
    EventInfo event1 = new EventInfo().eventType(EventType.TASK)
        .start(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 8, 0));
    Model model = new Model();
    model.getEventInfosReal().add(event1);

    Assert.assertNotNull(
        model.findEventAfter(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 7, 0)));
    Assert.assertNull(
        model.findEventAfter(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 8, 0)));
    Assert.assertNull(
        model.findEventAfter(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 9, 0)));
  }

}
