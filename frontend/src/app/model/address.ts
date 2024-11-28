export class Address {
  constructor(
    public country: string,
    public city: string,
    public postalCode: string,
    public street: string,
    public id?: string,
    public state?: string
  ) {}
}
