#ifdef _WIN32
#include <windows.h>             //bit ce ukljuceno ako se koriste windows
#endif

#include <GL/glut.h>

#include <iostream>
#include <math.h>
#include <fstream>
#include <stdlib.h>

using namespace std;

void myDisplay		();
void myReshape      (int width, int height);
void myMouse        (int button, int state, int x, int y);
void myKeyboard     (unsigned char theKey, int mouseX, int mouseY);
void mandelbrot     (GLdouble umin, GLdouble umax, GLdouble vmin, GLdouble vmax, 
                    GLint xmin, GLint xmax, GLint ymin, GLint ymax);
void napuni_rgb     ();
void rainbow_rgb    (); 
void mono_rgb       ();
void m_rgb          ();
void one_rgb        ();
void happy_rgb      ();

char skup;
GLdouble eps = 100;
GLint m = 32;
GLdouble Cx = 0;
GLdouble Cy = 0;
GLdouble ConstX = 0.155114;
GLdouble ConstY = -0.470914;
GLdouble umin, umax, w = 4; 
GLdouble vmin, vmax;

GLuint window; 
GLuint sub_width = 512, sub_height = 512;

static float fractal_rgb[255][3];
/*
void unesi_zajednicke_parametre(){
    cout << "eps, m ?" << endl;
    cin >> eps;
	cin >> m;

	cout << "Središnja tocka (x y) ?" << endl;
	cin >> Cx;
	cin >> Cy;

	cout << "Sirina pogleda?s " << endl;
	cin >> w;
}
*/
int main(int argc, char ** argv) {

    m_rgb();
	/* unos parametara */
	// cout << "Mandelbrotov ili Julijev skup (m/j) ?" << endl;
	// cin >> skup;

  cout << "Mandelbrotov skup - primjer unosa(0, 0, 0.45)" << endl;
  skup = 'm';
	if(skup == 'm' || skup == 'M') {
       /* cout << "eps, m ?" << endl;
        cin >> eps;
		cin >> m;
        */
		cout << "Center (x y) ?" << endl;
		cin >> Cx;
		cin >> Cy;

		cout << "Sirina pogleda? " << endl;
		cin >> w;
		
		
		cout << "q/e - zoom in/out" << endl;
		cout << "w,a,s,d - gibanje po skupu" << endl;
		cout << "x - centriranje u toèku (0, 0, 0.45)" << endl;
		cout << "x - centriranje u toèku (0, 0, 0.45)" << endl;
		cout << "f - dugine boje" << endl;
		cout << "z - random monokromatska paleta boja" << endl;
		cout << "c - random r, g ili b paleta boja" << endl;
		cout << "r - random paleta boja" << endl;
		cout << "\\ (eng layout) - centriranje u toèku (0, 0, 0.45)" << endl;
	}/* else if(skup == 'j' || skup == 'J') {

		cout << "Realni i imaginarni dio (Re Im) ?" << endl;
		cin >> ConstX;
		cin >> ConstY;
	} else {
        cerr << "Pogreska u odabiru" << endl;
		return 1;
	}*/
	
	//openGl
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
	glutInitWindowSize(sub_width,sub_height);
	glutInitWindowPosition(400, 200);
	glutInit(&argc, argv);

	window = glutCreateWindow("Fraktali");
	glutReshapeFunc(myReshape);
	glutDisplayFunc(myDisplay);
	glutMouseFunc(myMouse);
    glutKeyboardFunc(myKeyboard);

	glutMainLoop();
    return 0;
}

void myDisplay()
{
 	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	if(skup == 'm' || skup == 'M') {
		mandelbrot(Cx-w/.2, Cx+w/.2, Cy-w/.2, Cy+w/.2, 0, sub_width, 0, sub_height );
	} else {
	//	julij(100, 255, ConstX, ConstY, Cx-w/.2, Cx+w/.2, Cy-w/.2, Cy-w/.2, 0, sub_width, 0, sub_height);
	}

	glFlush();
}

void myReshape(int width, int height)
{
	sub_width = width;                      	 // promjena sirine prozora
    sub_height = height;				         // promjena visine prozora

    glViewport(0, 0, sub_width, sub_height);	 // otvor u prozoru
	glMatrixMode(GL_PROJECTION);			     //	matrica projekcije
	glLoadIdentity();				             //	jedinicna matrica
	gluOrtho2D(0, sub_width, 0, sub_height); 	 //	okomita projekcija
	glClearColor( 1.0f, 1.0f, 1.0f, 0.0f );		 //	boja pozadine
	glClear( GL_COLOR_BUFFER_BIT );		         //	brisanje pozadine
	glPointSize(1.0);				             //	postavi velicinu tocke
	glColor3f(0.0f, 0.0f, 0.0f);			     //	postavi boju
}


//- ucitat umin, umax), (v min, vmax).
//- x max, ymax (razlucivost zaslona)

void mandelbrot(GLdouble umin, GLdouble umax, GLdouble vmin, GLdouble vmax, 
                GLint xmin, GLint xmax, GLint ymin, GLint ymax) {
	int x, y, k;
	double u, v;
	double z_re, z_im;
	double c_re, c_im;
	double a, b;
	double deltau, deltav;

	eps = eps*eps;
	deltau = (double)(umax-umin) / (double)xmax;
	deltav = (double) (vmax-vmin) / (double)ymax;
	v = (double)ymin*(vmax-vmin) / ymax + vmin;
	
	glBegin(GL_POINTS);
	for(y = ymin; y <= ymax; y++) {
		u = (double)xmin * (umax-umin) / xmax + umin;
		for(x = xmin; x <= xmax; x++) {
			c_re = u;
			c_im = v;
			z_re = z_im = 0.;
			k = -1;
			
			do {
				k++;
				a = z_re * z_re - z_im * z_im + c_re;
				b = 2. * z_re * z_im + c_im;
				z_re = a;
				z_im = b;
			} while(a*a + b*b < eps && k < m);
			
			k = 255 - k;
			//Pixels[x][y] = RGB( 1.30727E-3*k*k - 0.8313931 * k + 255,
			//					4.25231E-3*k*k - 1.83336 * k + 255,
			//					7.4591E-4 * k * k + 6.07737E-2 * k );
			
			//glColor3f(			(1.30727E-3*k*k - 0.8313931 * k + 255) / 255.,
			//					(4.25231E-3*k*k - 1.83336 * k + 255) / 255.,
			//					(7.4591E-4 * k * k + 6.07737E-2 * k) / 255. );
			//float t = k / 255.0f;
			glColor3f(fractal_rgb[k][0], fractal_rgb[k][1], fractal_rgb[k][2]);
            glVertex2i(x, y);
			//cout << "draw (" << x << ", " << y << ")" << endl;

			u += deltau;
		}
		v += deltav;
	}
	glEnd();
}

void myMouse(int button, int state, int x, int y) {}

void myKeyboard(unsigned char theKey, int mouseX, int mouseY) {
    if (theKey == 'q') {
        w /= 1.2f;
        if (m < 150)
        m *= 1.1;
    } else if (theKey == 'e') {
        w *= 1.2f;
        if (m > 32)
        m /= 1.1;
    }
    else if (theKey == 'a') {
        Cx -= 0.01;
    }
    else if(theKey == 'd') {
        Cx += 0.01;
    }
    else if(theKey == 'w') {
        Cy += 0.01;
    }
    else if(theKey == 's') {
        Cy -= 0.01;
    }
    else if(theKey == 'r') {
        napuni_rgb();
    }
    else if(theKey == 'f') {
        rainbow_rgb();
    }
    else if(theKey == 'x') {
        Cx = 0;
        Cy = 0;
        w = 0.45;
        m = 32;
    }
    else if(theKey == 'z') {
        mono_rgb();
    }
    else if(theKey == 'c') {
        one_rgb();
    }
    else if(theKey == '\\') {
        happy_rgb();
    }
    
    glutPostRedisplay();
}

void napuni_rgb() {
    srand((unsigned int)time(NULL));
    for (int i = 0; i < 255; i++) {
        for (int j = 0; j < 3; j++) {
            fractal_rgb[i][j] = (rand() % 256) / 255.0f ;        
        }
    }
}

void one_rgb() {
    srand((unsigned int)time(NULL));
    int x = rand() % 3;
    for (int i = 0; i < 255; i++) {
        fractal_rgb[i][0] = 0;
        fractal_rgb[i][1] = 0;
        fractal_rgb[i][2] = 0;
        fractal_rgb[i][x] = (rand() % 256) / 255.0f ;        
    }
}

void rainbow_rgb() {
    float freq = 0.3f;
    
    for (int i = 0; i < 255; i++) {
        for (int j = 0; j < 3; j++) {
            fractal_rgb[i][j] = (sin(freq*i + j*2) * 127 + 128) / 255.0f;       
        }
    }
}

void mono_rgb() {
    for (int i = 0; i < 255; i++)
        fractal_rgb[i][0] =  fractal_rgb[i][1] = fractal_rgb[i][2] = (rand() % 256) / 255.0f ;
}

void m_rgb() {
    for (int i = 0; i < 255; i++) 
        fractal_rgb[i][0] =  fractal_rgb[i][1] = fractal_rgb[i][2] = ((rand() % 55)+ 200) / 255.0f ;
}

void happy_rgb() {
    float freq = 0.3f;
    
    for (int i = 0; i < 255; i++) {
        for (int j = 0; j < 3; j++) {
            fractal_rgb[i][j] = 0.5 * (1 + sin(freq*i + j));       
        }
    }    
}
