import { ConsultationSystemUIPage } from './app.po';

describe('consultation-system-ui App', function() {
  let page: ConsultationSystemUIPage;

  beforeEach(() => {
    page = new ConsultationSystemUIPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
