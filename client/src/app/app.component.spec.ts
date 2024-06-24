import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {AuthService} from "./shared/services/auth/auth.service";
import {of} from "rxjs";
import {IUser} from "../../models/IUser";

describe('AppComponent', () => {
  let authService: any;
  let document;
  let user: IUser = {
    name: "name"
  }

  beforeEach(async () => {
    document = {
      location: {
        href: ""
      }
    }
    authService = {checkAuth: jasmine.createSpy().and.returnValue(of(user))}
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        {
          provide: AuthService,
          useValue: authService
        },
        {
          provide: Document,
          useValue: document
        }
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'foodOrder' title`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('foodOrder');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Hello, foodOrder');
  });
});
