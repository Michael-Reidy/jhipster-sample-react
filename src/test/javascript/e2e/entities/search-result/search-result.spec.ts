import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import SearchResultComponentsPage, { SearchResultDeleteDialog } from './search-result.page-object';
import SearchResultUpdatePage from './search-result-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('SearchResult e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let searchResultComponentsPage: SearchResultComponentsPage;
  let searchResultUpdatePage: SearchResultUpdatePage;
  let searchResultDeleteDialog: SearchResultDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load SearchResults', async () => {
    await navBarPage.getEntityPage('search-result');
    searchResultComponentsPage = new SearchResultComponentsPage();
    expect(await searchResultComponentsPage.getTitle().getText()).to.match(/Search Results/);
  });

  it('should load create SearchResult page', async () => {
    await searchResultComponentsPage.clickOnCreateButton();
    searchResultUpdatePage = new SearchResultUpdatePage();
    expect(await searchResultUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /jhipsterSampleReactApp.searchResult.home.createOrEditLabel/
    );
    await searchResultUpdatePage.cancel();
  });

  it('should create and save SearchResults', async () => {
    async function createSearchResult() {
      await searchResultComponentsPage.clickOnCreateButton();
      await searchResultUpdatePage.setSourceInput('source');
      expect(await searchResultUpdatePage.getSourceInput()).to.match(/source/);
      await searchResultUpdatePage.setTextInput('text');
      expect(await searchResultUpdatePage.getTextInput()).to.match(/text/);
      await searchResultUpdatePage.setUrlInput('url');
      expect(await searchResultUpdatePage.getUrlInput()).to.match(/url/);
      await waitUntilDisplayed(searchResultUpdatePage.getSaveButton());
      await searchResultUpdatePage.save();
      await waitUntilHidden(searchResultUpdatePage.getSaveButton());
      expect(await searchResultUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createSearchResult();
    await searchResultComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await searchResultComponentsPage.countDeleteButtons();
    await createSearchResult();

    await searchResultComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await searchResultComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last SearchResult', async () => {
    await searchResultComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await searchResultComponentsPage.countDeleteButtons();
    await searchResultComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    searchResultDeleteDialog = new SearchResultDeleteDialog();
    expect(await searchResultDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /jhipsterSampleReactApp.searchResult.delete.question/
    );
    await searchResultDeleteDialog.clickOnConfirmButton();

    await searchResultComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await searchResultComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
