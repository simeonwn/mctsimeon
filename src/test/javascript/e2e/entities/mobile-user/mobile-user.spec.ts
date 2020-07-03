import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { MobileUserComponentsPage, MobileUserDeleteDialog, MobileUserUpdatePage } from './mobile-user.page-object';

const expect = chai.expect;

describe('MobileUser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let mobileUserComponentsPage: MobileUserComponentsPage;
  let mobileUserUpdatePage: MobileUserUpdatePage;
  let mobileUserDeleteDialog: MobileUserDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MobileUsers', async () => {
    await navBarPage.goToEntity('mobile-user');
    mobileUserComponentsPage = new MobileUserComponentsPage();
    await browser.wait(ec.visibilityOf(mobileUserComponentsPage.title), 5000);
    expect(await mobileUserComponentsPage.getTitle()).to.eq('mctApp.mobileUser.home.title');
    await browser.wait(ec.or(ec.visibilityOf(mobileUserComponentsPage.entities), ec.visibilityOf(mobileUserComponentsPage.noResult)), 1000);
  });

  it('should load create MobileUser page', async () => {
    await mobileUserComponentsPage.clickOnCreateButton();
    mobileUserUpdatePage = new MobileUserUpdatePage();
    expect(await mobileUserUpdatePage.getPageTitle()).to.eq('mctApp.mobileUser.home.createOrEditLabel');
    await mobileUserUpdatePage.cancel();
  });

  it('should create and save MobileUsers', async () => {
    const nbButtonsBeforeCreate = await mobileUserComponentsPage.countDeleteButtons();

    await mobileUserComponentsPage.clickOnCreateButton();

    await promise.all([
      mobileUserUpdatePage.setMobileNumberInput('62840538319'),
      mobileUserUpdatePage.setFirstNameInput('firstName'),
      mobileUserUpdatePage.setLastNameInput('lastName'),
      mobileUserUpdatePage.setDateOfBirthInput('2000-12-31'),
      mobileUserUpdatePage.genderSelectLastOption(),
      mobileUserUpdatePage.setEmailInput('ht0@_oFt.kNyO.eTqV4.Irri4t.8R'),
    ]);

    expect(await mobileUserUpdatePage.getMobileNumberInput()).to.eq(
      '62840538319',
      'Expected MobileNumber value to be equals to 62840538319'
    );
    expect(await mobileUserUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await mobileUserUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await mobileUserUpdatePage.getDateOfBirthInput()).to.eq('2000-12-31', 'Expected dateOfBirth value to be equals to 2000-12-31');
    expect(await mobileUserUpdatePage.getEmailInput()).to.eq(
      'ht0@_oFt.kNyO.eTqV4.Irri4t.8R',
      'Expected Email value to be equals to ht0@_oFt.kNyO.eTqV4.Irri4t.8R'
    );

    await mobileUserUpdatePage.save();
    expect(await mobileUserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await mobileUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last MobileUser', async () => {
    const nbButtonsBeforeDelete = await mobileUserComponentsPage.countDeleteButtons();
    await mobileUserComponentsPage.clickOnLastDeleteButton();

    mobileUserDeleteDialog = new MobileUserDeleteDialog();
    expect(await mobileUserDeleteDialog.getDialogTitle()).to.eq('mctApp.mobileUser.delete.question');
    await mobileUserDeleteDialog.clickOnConfirmButton();

    expect(await mobileUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
