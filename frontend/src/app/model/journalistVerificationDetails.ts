export class JournalistVerificationDetails {
  constructor(
    public id: string,
    public employer?: string,
    public distributionReach?: number,
    public mainMedium?: string,
    public reference?: URL,
    public message?: string
  ) {
  }
}
