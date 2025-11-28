import { HttpInterceptorFn } from '@angular/common/http';

/**
 * Interceptor HTTP para agregar credenciales (cookies) a todas las peticiones
 * Esto es necesario para que Spring Security mantenga la sesión
 */
export const credentialsInterceptor: HttpInterceptorFn = (req, next) => {
  // Clonar la petición y agregar withCredentials: true
  const clonedRequest = req.clone({
    withCredentials: true
  });

  return next(clonedRequest);
};

