import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerSpring implements MouseMotionListener, KeyListener {

	/**
	 * Little Wall Rock Climbing Copyright 2009 Eric McCreath GNU LGPL
	 */

	ArrayList<Spring> springs;
	ArrayList<Joint> joints;
	Points points;
	Points velocity;
	Points forces;

	XYPoint lhf, rhf, lff, rff, bf;

	Wall wall;
	Point lastpoint;
	boolean leftaction, rightaction, leftfaction, rightfaction, bodyaction;

	boolean move=false;

	static final double height = 1.85;
	static final double torso = 1.0;

	XYPoint lh, le, ls, n, p, ln, lf, rh, re, rs, rn, rf;
	Spring lRad, lHum, lClav, rRad, rHum, rClav, Lum1, Lum2, lFem, lTib, rFem,
			rTib;

	public PlayerSpring(Wall w) {
		this.wall = w;
		points = new Points();
		points.add(lh = new XYPoint(-0.5, 2.0));
		points.add(le = new XYPoint(-0.3, 1.7));
		points.add(ls = new XYPoint(-0.2, 1.58));

		points.add(rh = new XYPoint(0.5, 2.0));
		points.add(re = new XYPoint(0.3, 1.7));
		points.add(rs = new XYPoint(0.2, 1.58));

		points.add(n = new XYPoint(0.0, 1.58));
		points.add(p = new XYPoint(0.0, 1.0));

		points.add(rn = new XYPoint(0.2, 0.7));
		points.add(rf = new XYPoint(0.2, 0.2));

		points.add(ln = new XYPoint(-0.2, 0.7));
		points.add(lf = new XYPoint(-0.2, 0.2));

		double k = 0.4;

		springs = new ArrayList<Spring>();

		springs.add(lClav = new Spring(0.2, k, n, ls, points));
		springs.add(lHum = new Spring(0.3, k, ls, le, points));
		springs.add(lRad = new Spring(0.33, k, le, lh, points));

		springs.add(rClav = new Spring(0.2, k, n, rs, points));
		springs.add(rHum = new Spring(0.3, k, rs, re, points));
		springs.add(rRad = new Spring(0.33, k, re, rh, points));

		springs.add(Lum1 = new Spring(0.58, k, n, p, points));
		springs.add(Lum2 = new Spring(0.58, k, p, n, points));

		springs.add(lFem = new Spring(0.44, k, p, ln, points));
		springs.add(lTib = new Spring(0.5, k, ln, lf, points));

		springs.add(rFem = new Spring(0.44, k, p, rn, points));
		springs.add(rTib = new Spring(0.5, k, rn, rf, points));

		joints = new ArrayList<Joint>();
		joints.add(new Joint(Lum2, lClav, Math.PI / 2.0, Math.PI / 2.0));
		joints.add(new Joint(Lum2, rClav, -Math.PI / 2.0, -Math.PI / 2.0));

		joints.add(new Joint(rClav, rHum, -Math.PI / 2.0, Math.PI / 2.0));
		joints.add(new Joint(rHum, rRad, 0.0, Math.PI / 2.0));

		joints.add(new Joint(lClav, lHum, -Math.PI / 2.0, Math.PI / 2.0));
		joints.add(new Joint(lHum, lRad, -Math.PI / 2.0, 0.0));

		joints.add(new Joint(Lum1, rFem, 0.1, 0.4));
		joints.add(new Joint(rFem, rTib, -0.2, 0.1));

		joints.add(new Joint(Lum1, lFem, -0.4, -0.1));
		joints.add(new Joint(lFem, lTib, -0.1, 0.2));

		points.translate(new XYPoint(5.0, 1.0));

		forces = new Points(points.size());
		velocity = new Points(points.size());
		velocity.zero();

		lhf = new XYPoint(0.0, 0.0);
		rhf = new XYPoint(0.0, 0.0);
		lff = new XYPoint(0.0, 0.0);
		rff = new XYPoint(0.0, 0.0);
		bf = new XYPoint(0.0, 0.0);

		leftaction = false;

		for (Spring s : springs) {
			XYPoint p1 = points.get(s.p1index);
			XYPoint p2 = points.get(s.p2index);
			XYPoint dif1 = s.p1distance().scale(0.6);
			XYPoint dif2 = s.p1distance().scale(-0.3);
			p2.translate(dif1);
			p1.translate(dif2);
		}

		for (Joint j : joints) {
			XYPoint v1 = j.s1.vector();
			XYPoint v2 = j.s2.vector();

			double angle = v1.angle(v2);

			if (angle < j.mina) {
				XYPoint v2r = v2.rotate(-(angle - j.mina));
				points.get(j.s2.p2index).set(points.get(j.s2.p1index).add(v2r));
			} else if (angle > j.maxa) {
				XYPoint v2r = v2.rotate((j.maxa - angle));
				points.get(j.s2.p2index).set(points.get(j.s2.p1index).add(v2r));
			}
		}
	}

	public void draw(Graphics2D of) {

		of.setTransform(wall.trans);
		of.setColor(Color.black);
		of.setStroke(new BasicStroke(0.01f));

		// This is the section of code you need to modify to draw a more
		// realistic climber.


		XYPoint ld,rd,h1,h2,h3,h4,h5,hm,hm1,hm2,lhp1,lhp2,lhp3,rhp1,rhp2,rhp3;


		Spring Lhd,Rhd,Rhd2,hr1,hr2,hr3,hr4,hr5,lhs1,lhs2,lhs3,rhs1,rhs2,rhs3;
		double k = 0.4;



		//TODO step3 Rope
		Timer timer=new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				long nowtime = System.currentTimeMillis();
				if(nowtime-lasttime > 10000){
					move=false;

				}
			}
		},10000,100000);
		if(move){
			of.setColor(Color.green);
			Path2D p2 = new Path2D.Double();
			p2.moveTo(ln.getX(),ln.getY());
			p2.quadTo(ln.getX()-5,ln.getY()-5,8,600);
			of.draw(p2);
		}


		//head
		points.add(ld = new XYPoint(n.getX()-0.15, n.getY()+0.15));
		points.add(rd = new XYPoint(n.getX()+0.15, n.getY()+0.15));

		//hair
		points.add(h1 = new XYPoint(n.getX()-0.12, n.getY()+0.20));
		points.add(h2 = new XYPoint(n.getX()+0.18, n.getY()+0.20));
		points.add(h3 = new XYPoint(n.getX()+0.05, n.getY()+0.20));
		points.add(h4 = new XYPoint(n.getX(), n.getY()+0.20));
		points.add(h5 = new XYPoint(n.getX()+0.11, n.getY()+0.20));
		points.add(hm = new XYPoint(n.getX(), n.getY()+0.15));
		points.add(hm1 = new XYPoint(n.getX()-0.07, n.getY()+0.15));
		points.add(hm2 = new XYPoint(n.getX()+0.07, n.getY()+0.15));

		//hand
		points.add(lhp1 = new XYPoint(lh.getX()-0.06, lh.getY()-0.06));
		points.add(lhp2 = new XYPoint(lh.getX()+0.06, lh.getY()+0.06));
		points.add(lhp3 = new XYPoint(lh.getX()-0.04, lh.getY()+0.04));

		points.add(rhp1 = new XYPoint(rh.getX()-0.06, rh.getY()+0.06));
		points.add(rhp2 = new XYPoint(rh.getX()+0.06, rh.getY()-0.06));
		points.add(rhp3 = new XYPoint(rh.getX()+0.06, rh.getY()+0.06));



		//head
		Lhd=new Spring(0.58, k, n, ld, points);
		Rhd = new Spring(0.58, k, n, rd, points);
		Rhd2 = new Spring(0.58, k, ld, rd, points);

		//hair
		hr1=new Spring(0.58, k, ld, h1, points);
		hr2 = new Spring(0.58, k, rd, h2, points);
		hr3 = new Spring(0.58, k, hm, h3, points);
		hr4 = new Spring(0.58, k, hm1, h4, points);
		hr5 = new Spring(0.58, k, hm2, h5, points);

		double angle1 = lh.angle()-le.angle();
		double angle2 = rh.angle()-re.angle();
		lhp1.rotate(angle1);
		lhp2.rotate(angle1);
		lhp3.rotate(angle1);

		rhp1.rotate(angle2);
		rhp2.rotate(angle2);
		rhp3.rotate(angle2);

		//hand
		lhs1=new Spring(0.58, k, lhp1, lhp2, points);
		lhs2 = new Spring(0.58, k, lhp1, lhp3, points);
		lhs3 = new Spring(0.58, k, lhp2, lhp3, points);

		rhs1=new Spring(0.58, k, rhp1, rhp2, points);
		rhs2 = new Spring(0.58, k, rhp1, rhp3, points);
		rhs3 = new Spring(0.58, k, rhp2, rhp3, points);



		for (Spring s : springs) {
			if(s == lFem || s == lClav || s == rFem || s == rClav || s == lHum || s == rHum){
				of.setStroke(new BasicStroke(0.1f));
			}
			else if(s == Lum1 || s == Lum2){
				of.setStroke(new BasicStroke(0.1f));
				of.setColor(Color.green);
			}
			s.draw(of);
			of.setStroke(new BasicStroke(0.01f));
			of.setColor(Color.black);
			Lhd.draw(of);
			Rhd.draw(of);
			Rhd2.draw(of);
			hr1.draw(of);
			hr2.draw(of);
			hr3.draw(of);
			hr4.draw(of);
			hr5.draw(of);

			lhs1.draw(of);
			lhs2.draw(of);
			lhs3.draw(of);
			rhs1.draw(of);
			rhs2.draw(of);
			rhs3.draw(of);

		}


		of.setColor(Color.red);
		lRad.draw(of);
		of.setColor(Color.blue);
		lTib.draw(of);

		of.setColor(Color.yellow);
		of.setStroke(new BasicStroke(0.1f));
		new Spring(0.58, k, lf, lf, points).draw(of);
		new Spring(0.58, k, rf, rf, points).draw(of);

	}

	public void update(GameComponent canvas, Wall wall) {
		/*
		 * forces.zero(); for (XYPoint f : forces) { f.translate(new
		 * XYPoint(0.0,-0.001)); }
		 * 
		 * forces.get(points.indexOf(lh)).translate(lhf); lhf.zero();
		 * 
		 * // spring force for (Spring s : springs) {
		 * forces.get(s.p1index).translate(s.p1force());
		 * forces.get(s.p2index).translate(s.p2force()); }
		 * 
		 * // damperner for (int i = 0; i< springs.size(); i++) { Spring s =
		 * springs.get(i); XYPoint v1 = forces.get(s.p1index); XYPoint v2 =
		 * forces.get(s.p2index); XYPoint vel1 = velocity.get(s.p1index);
		 * XYPoint vel2 = velocity.get(s.p2index);
		 * 
		 * XYPoint dif = vel1.sub(vel2); XYPoint v1d = dif.scale(0.1); XYPoint
		 * v2d = dif.scale(-0.1); v1.translate(v2d); v2.translate(v1d); }
		 * 
		 * 
		 * for (int i = 0; i< velocity.size(); i++) {
		 * velocity.get(i).translate(forces.get(i)); }
		 * 
		 * 
		 * 
		 * 
		 * for (int i = 0; i< points.size(); i++) { XYPoint p = points.get(i);
		 * p.translate(velocity.get(i)); if (p.y < 0.2) { p.y = 0.2; } }
		 */

		if (lhf.length() > 0.0) {
			movelim(lhf, lh, le, ls, lRad, lHum);
		}

		if (rhf.length() > 0.0) {
			movelim(rhf, rh, re, rs, rRad, rHum);
		}

		if (lff.length() > 0.0) {
			movelim(lff, lf, ln, p, lFem, lTib);
		}

		if (rff.length() > 0.0) {
			movelim(rff, rf, rn, p, rFem, rTib);
		}

		if (bf.length() > 0.0) {
			if (canmovelim(bf, rh, re, rs, rRad, rHum)
					&& canmovelim(bf, lf, ln, p, lFem, lTib)
					&& canmovelim(bf, lh, le, ls, lRad, lHum)
					&& canmovelim(bf, rf, rn, p, rFem, rTib)) {
				domovelim(bf, rh, re, rs, rRad, rHum);
				domovelim(bf, lf, ln, p, lFem, lTib);
				domovelim(bf, lh, le, ls, lRad, lHum);
				domovelim(bf, rf, rn, p, rFem, rTib);
				p.translate(bf);
				n.translate(bf);
				ls.translate(bf);
				rs.translate(bf);
				bf.zero();
			}

		}

		/*
		 * // reset the lengths for (Spring s : springs) { XYPoint p1 =
		 * points.get(s.p1index); XYPoint p2 = points.get(s.p2index); XYPoint
		 * dif1 = s.p1distance().scale(0.6); XYPoint dif2 =
		 * s.p1distance().scale(-0.3); p2.translate(dif1); p1.translate(dif2); }
		 * 
		 * // limit the angles
		 * 
		 * for (Joint j : joints) { XYPoint v1 = j.s1.vector(); XYPoint v2 =
		 * j.s2.vector();
		 * 
		 * double angle = v1.angle(v2);
		 * 
		 * 
		 * if (angle < j.mina) { XYPoint v2r = v2.rotate(-(angle-j.mina));
		 * points.get(j.s2.p2index).set(points.get(j.s2.p1index).add(v2r)); }
		 * else if (angle > j.maxa) { XYPoint v2r = v2.rotate((j.maxa-angle));
		 * points.get(j.s2.p2index).set(points.get(j.s2.p1index).add(v2r)); } }
		 */

	}

	private boolean canmovelim(XYPoint bf, XYPoint lh, XYPoint le, XYPoint ls,
			Spring lrad, Spring lhum) {
		XYPoint lsn = ls.add(bf);
		Circle lhc = new Circle(lh, lrad.length);
		Circle lsc = new Circle(lsn, lhum.length);
		XYPoint[] inter = lhc.intersect(lsc);
		return inter != null;
	}

	private void domovelim(XYPoint bf, XYPoint lh, XYPoint le, XYPoint ls,
			Spring lrad, Spring lhum) {
		XYPoint lsn = ls.add(bf);
		Circle lhc = new Circle(lh, lrad.length);
		Circle lsc = new Circle(lsn, lhum.length);
		XYPoint[] inter = lhc.intersect(lsc);
		if (inter != null) {
			double d1 = inter[0].distance(le);
			double d2 = inter[1].distance(le);
			if (d1 < d2) {
				le.set(inter[0]);
			} else {
				le.set(inter[1]);
			}
		}
	}

	private void movelim(XYPoint lhf, XYPoint lh, XYPoint le, XYPoint ls,
			Spring lrad, Spring lhum) {
		XYPoint lhn = lh.add(lhf);
		Circle lhc = new Circle(lhn, lrad.length);
		Circle lsc = new Circle(ls, lhum.length);

		XYPoint[] inter = lhc.intersect(lsc);

		// still need to check angle constraints

		if (inter != null) {
			lh.set(lhn);
			double d1 = inter[0].distance(le);
			double d2 = inter[1].distance(le);
			if (d1 < d2) {
				le.set(inter[0]);
			} else {
				le.set(inter[1]);
			}
		}
		lhf.zero();
	}

	public void mouseDragged(MouseEvent arg0) {
	}

	public void mouseMoved(MouseEvent m) {
		// System.out.println("Mouse move");

		double scale = 0.03;

		Point p = m.getPoint();
		if (lastpoint != null) {

			double xm = scale * (p.getX() - lastpoint.getX());
			double ym = scale * -(p.getY() - lastpoint.getY());

			if (leftaction) {
				lhf.x = xm;
				lhf.y = ym;
			} else if (rightaction) {
				rhf.x = xm;
				rhf.y = ym;

			} else if (leftfaction) {
				lff.x = xm;
				lff.y = ym;

			} else if (rightfaction) {
				rff.x = xm;
				rff.y = ym;

			} else if (bodyaction) {
				bf.x = xm;
				bf.y = ym;
			}
		}
		lastpoint = p;
	}

	long lasttime;


	public void keyPressed(KeyEvent k) {
		//TODO step3
		lasttime=k.getWhen();
		move=true;

		// System.out.println("Key pressed");
		int kc = k.getKeyCode();
		if (kc == KeyEvent.VK_A) {
			leftaction = true;
		} else if (kc == KeyEvent.VK_S) {
			rightaction = true;
		} else if (kc == KeyEvent.VK_Z) {
			leftfaction = true;
		} else if (kc == KeyEvent.VK_X) {
			rightfaction = true;
		} else if (kc == KeyEvent.VK_SPACE) {
			bodyaction = true;
		}
	}

	public void keyReleased(KeyEvent k) {
		// System.out.println("Key released");
		int kc = k.getKeyCode();
		if (kc == KeyEvent.VK_A) {
			leftaction = false;
		} else if (kc == KeyEvent.VK_S) {
			rightaction = false;
		} else if (kc == KeyEvent.VK_Z) {
			leftfaction = false;
		} else if (kc == KeyEvent.VK_X) {
			rightfaction = false;
		} else if (kc == KeyEvent.VK_SPACE) {
			bodyaction = false;
		}
	}

	public void keyTyped(KeyEvent arg0) {
	}

}
