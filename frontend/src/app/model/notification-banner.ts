export class NotificationBanner {
  constructor(
    public showBanner: boolean = false,
    public bannerMessage: string = '',
    public bannerType: 'success' | 'error' = 'success',
  ) {}

  public dismiss() {
    this.showBanner = false;
  }
}
