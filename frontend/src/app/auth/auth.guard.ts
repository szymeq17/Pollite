import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";
import {AuthService} from "../service/auth.service";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const user = this.authService.userValue;
    if (user) {
      return true;
    }

    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
}
