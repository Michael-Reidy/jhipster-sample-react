import { element, by, ElementFinder } from 'protractor';

export default class SearchResultUpdatePage {
  pageTitle: ElementFinder = element(by.id('jhipsterSampleReactApp.searchResult.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  sourceInput: ElementFinder = element(by.css('input#search-result-source'));
  textInput: ElementFinder = element(by.css('input#search-result-text'));
  urlInput: ElementFinder = element(by.css('input#search-result-url'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setSourceInput(source) {
    await this.sourceInput.sendKeys(source);
  }

  async getSourceInput() {
    return this.sourceInput.getAttribute('value');
  }

  async setTextInput(text) {
    await this.textInput.sendKeys(text);
  }

  async getTextInput() {
    return this.textInput.getAttribute('value');
  }

  async setUrlInput(url) {
    await this.urlInput.sendKeys(url);
  }

  async getUrlInput() {
    return this.urlInput.getAttribute('value');
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
